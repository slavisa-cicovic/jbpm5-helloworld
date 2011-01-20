package com.plugtree.jbpm5.hello;

import java.util.Properties;

import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseConfiguration;
import org.drools.KnowledgeBaseFactory;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.compiler.BPMN2ProcessFactory;
import org.drools.compiler.ProcessBuilderFactory;
import org.drools.io.ResourceFactory;
import org.drools.marshalling.impl.ProcessMarshallerFactory;
import org.drools.runtime.KnowledgeSessionConfiguration;
import org.drools.runtime.StatefulKnowledgeSession;
import org.drools.runtime.process.ProcessRuntimeFactory;
import org.jbpm.bpmn2.BPMN2ProcessProviderImpl;
import org.jbpm.marshalling.impl.ProcessMarshallerFactoryServiceImpl;
import org.jbpm.process.builder.ProcessBuilderFactoryServiceImpl;
import org.jbpm.process.instance.ProcessRuntimeFactoryServiceImpl;
import org.jbpm.process.instance.event.DefaultSignalManagerFactory;
import org.jbpm.process.instance.impl.DefaultProcessInstanceManagerFactory;
import org.junit.Test;


public class HelloWorldTest {

@Test
public void testEDPBM() {
    try {
			// load up the processes knowledge base
			KnowledgeBase kbase = readKnowledgeBase();
			//the following properties are needed as jBPM5 own POM uses Drools 5.2.0-alpha1 as a dependency instead of the latest version
			Properties properties = new Properties();
		    properties.put("drools.processInstanceManagerFactory", DefaultProcessInstanceManagerFactory.class.getName());
	        properties.put("drools.processSignalManagerFactory", DefaultSignalManagerFactory.class.getName());
	        KnowledgeSessionConfiguration config = KnowledgeBaseFactory.newKnowledgeSessionConfiguration(properties);
	        	
			StatefulKnowledgeSession ksession = kbase.newStatefulKnowledgeSession(config, null);
			// start a 101 processes to ensure that the temporal rules will fire up
				ksession.startProcess("com.plugtree.jbpm5.edbpm.demo.LoanProcess");	
		} catch (Throwable t) {
			t.printStackTrace();
		}
		
	}
	
	private static KnowledgeBase readKnowledgeBase() throws Exception {

        ProcessBuilderFactory.setProcessBuilderFactoryService(new ProcessBuilderFactoryServiceImpl());
        ProcessMarshallerFactory.setProcessMarshallerFactoryService(new ProcessMarshallerFactoryServiceImpl());
        ProcessRuntimeFactory.setProcessRuntimeFactoryService(new ProcessRuntimeFactoryServiceImpl());
        BPMN2ProcessFactory.setBPMN2ProcessProvider(new BPMN2ProcessProviderImpl());
        
       
        
//      
//        properties.put("drools.processInstanceManagerFactory", "org.jbpm.persistence.processinstance.JPAProcessInstanceManagerFactory");
//        properties.put("drools.processSignalManagerFactory", "org.jbpm.persistence.processinstance.JPASignalManagerFactory");
        
        KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
		kbuilder.add(ResourceFactory.newClassPathResource("loan.bpmn"), ResourceType.BPMN2);
		return kbuilder.newKnowledgeBase();
	}
	
}