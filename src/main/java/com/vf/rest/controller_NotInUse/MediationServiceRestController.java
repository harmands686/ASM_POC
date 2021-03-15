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
import org.springframework.web.bind.annotation.PutMapping;
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

	String JOBID="BulkJob1";

	@GetMapping("/hello1")
    public String sayhello() {
        return "Welcome1";
    }
	
	
    @GetMapping("/startBulkJob")
    public String initiateJob() throws ASMEdgeNotFoundException {

    	try{
		    	List<ASMResource> asmResources = (ArrayList<ASMResource>) asmResourceRepo.findByApp("gb");
		
		    	List<ASMEdge> asmEdges = (ArrayList<ASMEdge>) asmEdgeRepo.findByApp("gb");
		    	
		    	int statusCode1 = asm_checkJobAlreadyRunning(JOBID);
		    	if(statusCode1 != 200){// Job is not running.
		    		int statusCode2 = asm_startBulkJob(JOBID);
		    		if(statusCode2 != 201 ){// If not 201 then we failed
		    			System.out.println("ASM BulkJob1 failed to start!!!");
		    		}else{// Result code is 201, ie. successful.
		    			System.out.println("ASM BulkJob1 started successfully");
		    			asm_createResource(asmResources,JOBID);
		    			asm_createEdge(asmEdges,JOBID);
		    			int status3 = asm_syncBulkJob(JOBID);
		    			if (status3 != 200){ // If not 200 then we failed.
		    				System.out.println("ASM BulkJob1 Synchronized can not be completed.");
		    				
		    			  } else {  // Result code is 200, ie. successful.
		    				  System.out.println("ASM BulkJob1 Synchronized is completed successfully");
		    				  
		    			  }
		    		}
		    		
		    	}else{//Job already running
		    		System.out.println("ASM BulkJob1 already running!!!");
		    		return "ASM BulkJob1 already running!!!";
		    	}
		    	return "ASM BulkJob1 completed. To see status check logs!!!";
    	
    	}catch (Exception e) {
			// TODO: handle exception
    		e.printStackTrace();
    		return e.getMessage();
		}
    	
    }

    
    public int asm_checkJobAlreadyRunning(String jobId) throws Exception {

    	RestTemplate restTemplate = new RestTemplate();
    	String url = "http://noi-topology-rest-observer:9104/1.0/rest-observer/jobs/"+jobId;
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
    	ResponseEntity<String> result = restTemplate.exchange(new URI(url), HttpMethod.GET, entity, String.class);
    	System.out.println("JSON result<asm_checkJobAlreadyRunning>: "+result.getStatusCodeValue());
    	return result.getStatusCodeValue();
    	
    }
    
    public int asm_startBulkJob(String jobId) throws Exception {

    	RestTemplate restTemplate = new RestTemplate();
    	String url = "http://noi-topology-rest-observer:9104/1.0/rest-observer/jobs/bulk_replace";
    	HttpHeaders headers = new HttpHeaders();
    	headers.setContentType(MediaType.APPLICATION_JSON);
    	headers.set("X-TenantID", "cfd95b7e-3bc7-4006-a4a8-a73a79c71255");
    	headers.set("Content-Type", "application/json");
    	headers.set("Accept", "application/json");
    	String plainCredentials = "noi-topology-verizon-noi-user" + ":" + "IoSGUpTV0LbRLmlhg2IqMASA6Uo8DR/egKz7PKHm8UM=";
        String base64Credentials = new String(Base64.getEncoder().encode(plainCredentials.getBytes()));
        headers.set("Authorization", "Basic " + base64Credentials);
    	//headers.set("JobId", jobId);
    
    		
    	String jsonContent = "{\"unique_id\": " + "\"" + jobId + "\"" + ",\"parameters\": "  + "{" + "\"provider\": " + "\"" + "NOI_ImpactBulkReplaceJob" + "\"}}";
    	System.out.println("jsonContent>>"+jsonContent);
		HttpEntity<String> entity = new HttpEntity<String>(jsonContent,headers);
		//String answer = restTemplate.postForObject(url, entity, String.class);
		ResponseEntity<String> result = restTemplate.postForEntity(url, entity, String.class);
		System.out.println("JSON result<asm_startBulkJob>: "+result.getStatusCodeValue());
    	return result.getStatusCodeValue();
    }

    public int asm_syncBulkJob(String jobId) throws Exception {

    	RestTemplate restTemplate = new RestTemplate();
    	String url = "http://noi-topology-rest-observer:9104/1.0/rest-observer/jobs/"+jobId+"/synchronize";
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
		ResponseEntity<String> result = restTemplate.postForEntity(url, entity, String.class);
		System.out.println("JSON result<asm_startBulkJob>: "+result.getStatusCodeValue());
    	return result.getStatusCodeValue();
    }

    public void asm_createResource(List<ASMResource> resources,String jobId) throws Exception {

    	RestTemplate restTemplate = new RestTemplate();

    	String url = "http://noi-topology-rest-observer:9104/1.0/rest-observer/rest/resources";
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
    		ResponseEntity<String> result = restTemplate.postForEntity(url, entity, String.class);
    		System.out.println("JSON result<asm_creteResource>: "+result.getStatusCodeValue());
    	}
       
    }

    public void asm_createEdge(List<ASMEdge> edges,String jobId) throws Exception {

    	RestTemplate restTemplate = new RestTemplate();

    	String url = "http://noi-topology-rest-observer:9104/1.0/rest-observer/rest/references";
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
    		ResponseEntity<String> result = restTemplate.postForEntity(url, entity, String.class);
    		System.out.println("JSON result<asm_createEdge>: "+result.getStatusCodeValue());
    	}
       
    }

    
}