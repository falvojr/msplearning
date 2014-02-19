package com.msplearning.restful.app;

import java.io.File;
import java.util.Arrays;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.apache.maven.shared.invoker.DefaultInvocationRequest;
import org.apache.maven.shared.invoker.DefaultInvoker;
import org.apache.maven.shared.invoker.InvocationRequest;
import org.apache.maven.shared.invoker.InvocationResult;
import org.apache.maven.shared.invoker.Invoker;
import org.apache.maven.shared.invoker.MavenInvocationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.msplearning.entity.App;
import com.msplearning.entity.util.BusinessException;
import com.msplearning.restful.app.generic.CustomMediaType;
import com.msplearning.restful.app.generic.GenericCrudRESTfulServer;
import com.msplearning.service.AppService;
import com.msplearning.service.generic.GenericCrudService;

/**
 * The StudentRESTfulServer class provides the RESTful services of entity
 * {@link App}.
 * 
 * @author Venilton Falvo Junior (veniltonjr)
 */
@Component
@Path("/app")
public class AppRESTfulServer extends GenericCrudRESTfulServer<App, Long> {

	/**
	 * This field is set by Spring on context:property-placeholder configured in
	 * applicationContext.xml
	 */
	@Value("${project.basedirectory}")
	private String baseDirectory;

	@Autowired
	private AppService appService;

	@Override
	protected GenericCrudService<App, Long> getService() {
		return this.appService;
	}

	@POST
	@Produces(CustomMediaType.APPLICATION_ANDROID_PACKAGE_ARCHIVE)
	@Override
	public Response insert(App entity) {
		try {
			this.getService().insert(entity);

			InvocationRequest request = new DefaultInvocationRequest();
			request.setPomFile(new File(this.baseDirectory + "\\pom.xml"));
			request.setGoals(Arrays.asList("-DskipTests=true", "verify"));

			Invoker invoker = new DefaultInvoker();
			invoker.setMavenHome(new File(System.getenv("M3_HOME")));
			InvocationResult result = invoker.execute(request);

			if (result.getExitCode() == 0) {
				String path = this.baseDirectory + "\\android-app\\target\\android-app.apk";
				String apkName = entity.getName().replaceAll("\\W", "");
				if (apkName == "") {
					apkName = "MSPLearning";
				}
				return Response.ok(new File(path)).header("Content-Disposition", String.format("attachment;filename=%s.apk", apkName)).build();
			} else {
				return Response.serverError().build();
			}
		} catch (BusinessException businessException) {
			return Response.serverError().entity(businessException.getMessage()).build();
		} catch (MavenInvocationException mavenException) {
			return Response.serverError().build();
		}
	}
}
