package source;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.net.MalformedURLException;
import java.net.URL;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;

@Path("generic")
public class restservice {
	@SuppressWarnings("unused")
	@Context
	private UriInfo context;

	/**
	 * Default constructor. 
	 */
	public restservice() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * Retrieves representation of an instance of restservice
	 * @return an instance of String
	 */
	@GET
	//@Produces("application/json")
	@Produces("text/plain")
	public String getJson() {
		String result = "";
		try {
			//URL solr = new URL("http://api.openweathermap.org/data/2.5/weather?q=London,uk");
			//URL solr = new URL("http://localhost.localdomain:8983/solr/collection1_shard1_replica1/select?q=id%3Aumkc&wt=json&indent=true");
			URL solr = new URL("http://134.193.136.127:8983/solr/collection1_shard1_replica1/select?q=id%3Arest%0Aid%3Astomp%0A%0A&wt=json&indent=true");
			BufferedReader in = new BufferedReader(new InputStreamReader(solr.openStream()));
			String inputLine; 


			while ((inputLine = in.readLine()) != null) {

				result = result + inputLine;
			}
			in.close(); 

		} catch (MalformedURLException me) {
			System.out.println(me); 

		} catch (IOException ioe) {
			System.out.println(ioe);
		}
		return result;
	}
	@GET
	@Produces("application/x-javascript")
	@Path("hadoopPut/{hadoopInputDir}")
	public String hadoopPut(@QueryParam("callback") String callback, @PathParam("hadoopInputDir") String hadoopInputDir) {
		String line="";

		try
		{

			Process process = Runtime.getRuntime().exec ("hadoop fs -put /home/idcuser/test/testfile.txt /"+hadoopInputDir);
			// Process process = Runtime.getRuntime().exec ("pwd");
			InputStreamReader ir=new InputStreamReader(process.getInputStream());
			LineNumberReader input = new LineNumberReader (ir);

			while ((line = input.readLine ()) != null){
				System.out.println(line);
				line = line+"\n";
				return line;

			}


		}
		catch (java.io.IOException e){
			System.err.println ("IOException " + e.getMessage());
			return "IOException " + e.getMessage();
		}

		return line;
	}
	@GET
	@Produces("application/json")
	@Path("viewResult/{hadoopOutputDir}")
	public String getResult(@QueryParam("callback") String callback, @PathParam("hadoopOutputDir") String hadoopOutputDir) {
		String line="";

		StringBuffer sb = new StringBuffer();

		try
		{
			// Process process = Runtime.getRuntime().exec ("pwd");
			Process process = Runtime.getRuntime().exec ("hadoop fs -cat "+hadoopOutputDir+"/*00");
			BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));

			while ((line = br.readLine ()) != null){
				//System.out.println(line);

				sb.append(line).append("\n");
				// return line;

			}


		}
		catch (java.io.IOException e){
			System.err.println ("IOException " + e.getMessage());
			return "IOException " + e.getMessage();
		}

		return sb.toString();
	}
	@GET
	@Produces("application/x-javascript")
	@Path("uploadJson2Solr/{jsonfile:.+}")
	public String uploadJson2Solr(@QueryParam("callback") String callback,@PathParam("jsonfile") String jsonfile){

		String line="";

		try
		{
			// Process process = Runtime.getRuntime().exec ("pwd");
			Process process = Runtime.getRuntime().exec ("curl http://134.193.136.127:8983/solr/collection1_shard1_replica1/update/json?commit=true --data-binary @"+jsonfile+" -H Content-type:application/json");
			//	Process process = Runtime.getRuntime().exec ("curl www.google.com");
			InputStreamReader ir=new InputStreamReader(process.getInputStream());
			LineNumberReader i = new LineNumberReader (ir);

			while ((line = i.readLine ()) != null){
				System.out.println(line);
				line = line+"\n";
				return line;  	 
			}

		}
		catch (java.io.IOException e){
			System.err.println ("IOException " + e.getMessage());
			return "IOException " + e.getMessage();
		}

		return line;   	
	}

	/**
	 * PUT method for updating or creating an instance of restservice
	 * @param content representation for the resource
	 * @return an HTTP response with content of the updated or created resource.
	 */
	@PUT
	@Consumes("application/json")
	public void putJson(String content) {
	}

}