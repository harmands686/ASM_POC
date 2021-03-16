package com.vf.rest.controller_NotInUse;

import java.net.URI;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.vf.exception.ASMEdgeNotFoundException;
import com.vf.model.ASMEdge;
import com.vf.model.ASMResource;
import com.vf.repository.ASMEdgeRepository;
import com.vf.repository.ASMResourceRepository;

@RestController
public class MediationServiceRestController {

	@Autowired
    ASMEdgeRepository asmEdgeRepo;
	
	@Autowired
	ASMResourceRepository asmResourceRepo;

	
	String url1 = "https://noi-topology-rest-observer.verizon-noi.svc:9104/1.0/rest-observer/jobs/jobid_replace";
	String url2 = "https://noi-topology-rest-observer.verizon-noi.svc:9104/1.0/rest-observer/jobs/bulk_replace";
	String url3 = "https://noi-topology-rest-observer.verizon-noi.svc:9104/1.0/rest-observer/rest/resources";
	String url4 = "https://noi-topology-rest-observer.verizon-noi.svc:9104/1.0/rest-observer/rest/references";
	String url5 = "https://noi-topology-rest-observer.verizon-noi.svc:9104/1.0/rest-observer/jobs/jobid_replace/synchronize";
	
	@GetMapping("/hello1")
    public String sayhello() {
        return "Welcome1";
    }
	
	public boolean runJob(int statusCode){
		if(statusCode !=200){ //Job is not already running
			return true;
		}else
			return false; // Job is already running
	}
	
    @GetMapping("/startBulkJob/{id}/{provider}/{sync}")
    public String initiateJob(@PathVariable(value = "id") String JOBID,@PathVariable(value = "provider") String PROVIDER,@PathVariable(value = "sync") String SYNC) throws ASMEdgeNotFoundException {

    	
    	int statusCode2 = 0;
    	try{
		    	List<ASMResource> asmResources = (ArrayList<ASMResource>) asmResourceRepo.findByApp("gb");
		
		    	List<ASMEdge> asmEdges = (ArrayList<ASMEdge>) asmEdgeRepo.findByApp("gb");
		    	
		    	System.out.println("************Starting Bulk Replace job with job id "+JOBID);
		    	int statusCode1 = asm_checkJobAlreadyRunning(JOBID);
		    	System.out.println("StatusCode1="+statusCode1);
		    	if(runJob(statusCode1) == true){
		    		statusCode2 = asm_startBulkJob(JOBID,PROVIDER);
		    		System.out.println("statusCode2="+statusCode2);
		    		System.out.println("Sleeping for 5 seconds");
		    		Thread.sleep(5000);
		    	}
		    	else{//Job already running
		    		System.out.println("ASM "+JOBID+ " already running!!!");
		    		//return "ASM "+JOBID+" already running!!!";
		    		statusCode2 = 201; //Send the latest resources and edges again
		    	}
		    	if(statusCode2 != 201 ){// If not 201 then we failed
		    			System.out.println("ASM "+JOBID+" failed to start!!!");
		    	}else{// Result code is 201, ie. successful.
		    			System.out.println("ASM "+JOBID+" started successfully");
		    			asm_createResource(asmResources,JOBID);
		    			//System.out.println("Sleeping for 5 seconds");
		    			//Thread.sleep(5000);
		    			asm_createEdge(asmEdges,JOBID);
		    			if(SYNC.equalsIgnoreCase("y"))
		    			{
		    				int status3 = asm_syncBulkJob(JOBID);
			    			System.out.println("statusCode3="+status3);
			    			if (status3 != 200){ // If not 200 then we failed.
			    				System.out.println("ASM "+JOBID+" Synchronized can not be completed.");
			    				
			    			  } else {  // Result code is 200, ie. successful.
			    				  System.out.println("ASM "+JOBID+" Synchronized is completed successfully");
			    				  
			    			  }
		    			}
		    	}
		    		
		    	return "ASM " +JOBID+ " completed. Sync="+ SYNC+" To see status check logs!!!";
    	
    	}catch (Exception e) {
			// TODO: handle exception
    		e.printStackTrace();
    		return e.getMessage();
		}
    	
    }

    
    public int asm_checkJobAlreadyRunning(String jobId) {

    	int statusCode = 0;
    	ResponseEntity<String> result = null;
    	try{
    	RestTemplate restTemplate = new RestTemplate();
    	HttpHeaders headers = new HttpHeaders();
    	//headers.setContentType(MediaType.APPLICATION_JSON);
    	headers.set("X-TenantID", "cfd95b7e-3bc7-4006-a4a8-a73a79c71255");
    	//headers.set("Content-Type", "application/json");
    	headers.set("Accept", "application/json");
    	String plainCredentials = "noi-topology-verizon-noi-user" + ":" + "IoSGUpTV0LbRLmlhg2IqMASA6Uo8DR/egKz7PKHm8UM=";
        String base64Credentials = new String(Base64.getEncoder().encode(plainCredentials.getBytes()));
        headers.set("Authorization", "Basic " + base64Credentials);
    	//headers.set("JobId", jobId);
    		
    	HttpEntity<String> entity = new HttpEntity<String>(headers);
    	//String answer = restTemplate.postForObject(url, entity, String.class);
    	//ResponseEntity<String> result = restTemplate.getForEntity(url,entity, String.class);
    	result = restTemplate.exchange(new URI(url1.replaceAll("jobid_replace", jobId)), HttpMethod.GET, entity, String.class);
    	statusCode = result.getStatusCodeValue();
    	System.out.println("JSON result<asm_checkJobAlreadyRunning>: "+result.getStatusCodeValue());
    	}
    	catch(Exception ex){
    		ex.printStackTrace();
    	}
    	return statusCode;
    	
    }
    
    public int asm_startBulkJob(String jobId, String provider) {

    	int statusCode = 0;
    	ResponseEntity<String> result = null;
    	try{
    	RestTemplate restTemplate = new RestTemplate();
    	HttpHeaders headers = new HttpHeaders();
    	headers.setContentType(MediaType.APPLICATION_JSON);
    	headers.set("X-TenantID", "cfd95b7e-3bc7-4006-a4a8-a73a79c71255");
    	headers.set("Content-Type", "application/json");
    	headers.set("Accept", "application/json");
    	String plainCredentials = "noi-topology-verizon-noi-user" + ":" + "IoSGUpTV0LbRLmlhg2IqMASA6Uo8DR/egKz7PKHm8UM=";
        String base64Credentials = new String(Base64.getEncoder().encode(plainCredentials.getBytes()));
        headers.set("Authorization", "Basic " + base64Credentials);
    	//headers.set("JobId", jobId);
    
    		
    	String jsonContent = "{\"unique_id\": " + "\"" + jobId + "\"" + ",\"parameters\": "  + "{" + "\"provider\": " + "\"" + provider+ "\"}}";
    	System.out.println("jsonContent>>"+jsonContent);
		HttpEntity<String> entity = new HttpEntity<String>(jsonContent,headers);
		//String answer = restTemplate.postForObject(url, entity, String.class);
		result = restTemplate.postForEntity(url2, entity, String.class);
		statusCode = result.getStatusCodeValue();
		System.out.println("JSON result<asm_startBulkJob>: "+result.getStatusCodeValue());
    	}
    	catch (Exception ex) {
    		ex.printStackTrace();
		}
    	return statusCode;
    }

    public int asm_syncBulkJob(String jobId) {

    	int statusCode = 0;
    	ResponseEntity<String> result = null;
    	try{
    	RestTemplate restTemplate = new RestTemplate();
    	HttpHeaders headers = new HttpHeaders();
    	headers.setContentType(MediaType.APPLICATION_JSON);
    	headers.set("X-TenantID", "cfd95b7e-3bc7-4006-a4a8-a73a79c71255");
    	headers.set("Content-Type", "application/json");
    	headers.set("Accept", "application/json");
    	String plainCredentials = "noi-topology-verizon-noi-user" + ":" + "IoSGUpTV0LbRLmlhg2IqMASA6Uo8DR/egKz7PKHm8UM=";
        String base64Credentials = new String(Base64.getEncoder().encode(plainCredentials.getBytes()));
        headers.set("Authorization", "Basic " + base64Credentials);
    	headers.set("JobId", jobId);
    
		HttpEntity<String> entity = new HttpEntity<String>(headers);
		//String answer = restTemplate.postForObject(url, entity, String.class);
		result = restTemplate.postForEntity(url5.replaceAll("jobid_replace", jobId), entity, String.class);
		statusCode = result.getStatusCodeValue();
		System.out.println("JSON result<asm_syncBulkJob>: "+statusCode);
    }
	catch (Exception ex) {
		ex.printStackTrace();
	}
	return statusCode;
    }

    public void asm_createResource(List<ASMResource> resources,String jobId) throws Exception {

    	RestTemplate restTemplate = new RestTemplate();

    	HttpHeaders headers = new HttpHeaders();
    	headers.setContentType(MediaType.APPLICATION_JSON);
    	headers.set("X-TenantID", "cfd95b7e-3bc7-4006-a4a8-a73a79c71255");
    	headers.set("Content-Type", "application/json");
    	headers.set("Accept", "application/json");
    	String plainCredentials = "noi-topology-verizon-noi-user" + ":" + "IoSGUpTV0LbRLmlhg2IqMASA6Uo8DR/egKz7PKHm8UM=";
        String base64Credentials = new String(Base64.getEncoder().encode(plainCredentials.getBytes()));
        headers.set("Authorization", "Basic " + base64Credentials);
    	headers.set("JobId", jobId);
    
    	for(ASMResource asmResource : resources){
    		
    		String jsonContent = "{\"name\": " + "\"" + asmResource.getUniqueId() + "\"" + ",\"uniqueId\": " + "\""+ asmResource.getUniqueId() + "\"" + ",\"matchTokens\": "  + "[" + "\"" + asmResource.getUniqueId() + "\"" + "]" + ",\"mergeTokens\": " + "[" + "\"" +  asmResource.getUniqueId() + "\"" + "]" + ",\"entityTypes\": "+  "[" + "\"" + asmResource.getEntityTypes() + "\"" + "]}";
    		System.out.println("jsonContent>>"+jsonContent);
    		
    		HttpEntity<String> entity = new HttpEntity<String>(jsonContent,headers);
    		//String answer = restTemplate.postForObject(url, entity, String.class);
    		ResponseEntity<String> result = restTemplate.postForEntity(url3, entity, String.class);
    		System.out.println("JSON result<asm_creteResource>: "+result.getStatusCodeValue());
    	}
       
    }

    public void asm_createEdge(List<ASMEdge> edges,String jobId) throws Exception {

    	RestTemplate restTemplate = new RestTemplate();

    	HttpHeaders headers = new HttpHeaders();
    	headers.setContentType(MediaType.APPLICATION_JSON);
    	headers.set("X-TenantID", "cfd95b7e-3bc7-4006-a4a8-a73a79c71255");
    	headers.set("Content-Type", "application/json");
    	headers.set("Accept", "application/json");
    	String plainCredentials = "noi-topology-verizon-noi-user" + ":" + "IoSGUpTV0LbRLmlhg2IqMASA6Uo8DR/egKz7PKHm8UM=";
        String base64Credentials = new String(Base64.getEncoder().encode(plainCredentials.getBytes()));
    	headers.set("Authorization", "Basic " + base64Credentials);
    	headers.set("JobId", jobId);
    
    	for(ASMEdge asmEdge : edges){
    		
    		String jsonContent = "{\"_fromUniqueId\": " + "\"" + asmEdge.getFromRes() + "\"" + ",\"_edgeType\": " + "\""+ asmEdge.getRelationShip() + "\"" + ",\"_toUniqueId\": " + "\""+ asmEdge.getToRes() + "\"}";
    		System.out.println("jsonContent>>"+jsonContent);	
    		HttpEntity<String> entity = new HttpEntity<String>(jsonContent,headers);
    		//String answer = restTemplate.postForObject(url, entity, String.class);
    		ResponseEntity<String> result = restTemplate.postForEntity(url4, entity, String.class);
    		System.out.println("JSON result<asm_createEdge>: "+result.getStatusCodeValue());
    	}
       
    }

    
}