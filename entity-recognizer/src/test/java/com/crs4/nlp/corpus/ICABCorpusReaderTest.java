package com.crs4.nlp.corpus;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectOutputStream;

import org.junit.Test;

import eu.danieldk.nlp.jitar.cli.CrossValidation;
import eu.danieldk.nlp.jitar.cli.Tag;
import eu.danieldk.nlp.jitar.corpus.CorpusReader;
import eu.danieldk.nlp.jitar.data.Model;
import eu.danieldk.nlp.jitar.training.FrequenciesCollector;

public class ICABCorpusReaderTest {
	@Test
	 public void testCorpusReader() throws IOException{
		         String nomeFile="src/test/resources/I-CAB_All/NER-09/I-CAB-evalita09-NER-training.iob2";
		         BufferedReader reader= new BufferedReader( new FileReader(nomeFile));
		        CorpusReader corpusReader = new ICABCorpusReader(reader, false);
		        FrequenciesCollector frequenciesCollector = new FrequenciesCollector();
		        frequenciesCollector.process(corpusReader);
		         Model model = frequenciesCollector.model();
		         ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("src/test/resources/icab.par"));
		           oos.writeObject(model);
	}
	
	@Test
	public void testTag(){
		
		String [] args={"src/test/resources/icab.par"};//,"il pozzo è vuoto"};
		Tag.main(args);
		
	}
	
	@Test
	public void crossValidation() throws IOException{
		String [] args={"icab","src/test/resources/I-CAB_All/NER-09/I-CAB-evalita09-NER-test.iob2"};
		
		CrossValidation.main(args);
	}

}
