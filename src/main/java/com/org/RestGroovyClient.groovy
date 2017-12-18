import groovy.lang.*
import groovy.util.slurpersupport.GPathResult

import groovyx.net.http.RESTClient
import java.sql.*
import spock.lang.*
import java.io.*
import groovyx.net.http.*


        String url=null
        String PMS_STATUS=null
        String analysisUrl="http://localhost:9000/api/qualitygates/project_status?analysisId="
        String analysisId=null
        def taskJson=null
        def status=null
        
        
        println "Start getting Qaulity Gate::"
        
        Class.forName("oracle.jdbc.driver.OracleDriver");
        
		Connection connection = DriverManager.getConnection("jdbc:oracle:thin:@//localhost:1521/xe", "****", "*******");
		
		Statement createStatement = connection.createStatement();
		
		ResultSet resultSet = createStatement.executeQuery("select * from T_PMS_STATUS");
		
		while (resultSet.next()) {
		    PMS_STATUS=resultSet.getString("PMS_STATUS")
			println "PMS_STATUS:: "+PMS_STATUS
			
		}

        new File("C:/Program Files (x86)/Jenkins/workspace/Spring Tutorial Packaging/target/sonar/report-task.txt").eachLine {  
            line -> 

            if ("$line".contains("ceTaskUrl")){
                
                    url="$line".replace("ceTaskUrl=", "");
                    println "Task URL::"+url
                    
                 } 
            } 
            // def client = new RESTClient("http://localhost:9000/api/ce/task?id=AWBkfQI9F3Uvw6WFPjAq");
            def client = new RESTClient(url);
            def json = client.get(path: 'task.json')
            taskJson=json.data.get("task")
            println taskJson
            status=taskJson.get("status")
            analysisId=taskJson.get("analysisId")
            
            println "Start Task Status::"+status+" analysisId::"+analysisId
            
            if(status.equalsIgnoreCase("PENDING")||status.equalsIgnoreCase("IN_PROGRESS")){
              
                println "Task Status is "+status
                println "Sleeping Thread for 20 sec!!"
                this.sleep(20000)
                client = new RESTClient(url);
                json = client.get(path: 'task.json') 
                taskJson=json.data.get("task")
                println taskJson
                status=taskJson.get("status")
                analysisId=taskJson.get("analysisId")
                
                
                println "Status after Sleep::"+status+" analysisId::"+analysisId

            }
            if(status.equalsIgnoreCase("SUCCESS")){
                
                println "Task Status::"+status
                println "Getting Analysis Report::"+analysisId
                
                String qaulityGate=this.getAnalysisStatus(analysisUrl,analysisId)
                
                if(qaulityGate.equalsIgnoreCase("ERROR")){
                    
                    throw new Exception("Qaulity Gate Failed");
                }
             
            }else if(status.equalsIgnoreCase("FAILED")||status.equalsIgnoreCase("CANCELED")){
                
                println "Failing Build for Task status ::"+status
                throw new Exception("Qaulity Gate Failed");
             
            }else{
                println status
                throw new Exception("Qaulity Gate Failed");
            
            }
            //def var='$ceTaskUrl
            //println var
            //waitForQualityGate()

        String getAnalysisStatus(String analysisUrl,String analysisId){
            
            println "getAnalysisStatus() Start::"
            
            def analysisFinalUrl=analysisUrl+analysisId
            
            println "analysisFinalUrl"+analysisFinalUrl
            client = new RESTClient(analysisFinalUrl);
            println client
            def json1 = client.get(path:'project_status.json') 
            println json1
            def projectStatusJson=json1.data.get("projectStatus")
            String analysisStatus=projectStatusJson.get("status")
            //analysisStatus=""
            return analysisStatus
        }

