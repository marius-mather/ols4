package uk.ac.ebi.owl2json.transforms;
import java.util.*;

import uk.ac.ebi.owl2json.OwlNode;
import uk.ac.ebi.owl2json.OwlTranslator;
import uk.ac.ebi.owl2json.properties.PropertyValue;

public class SynonymAnnotator {

	public static void annotateSynonyms(OwlTranslator translator) {

		long startTime3 = System.nanoTime();


		Set<String> synonymProperties = new TreeSet<String>();
		synonymProperties.add("http://www.geneontology.org/formats/oboInOwl#hasExactSynonym");


		Object configSynonymProperties = translator.config.get("synonym_property");

		if(configSynonymProperties instanceof Collection<?>) {
			synonymProperties.addAll((Collection<String>) configSynonymProperties);
		}


		for(String id : translator.nodes.keySet()) {
		    OwlNode c = translator.nodes.get(id);
		    if (c.types.contains(OwlNode.NodeType.CLASS) ||
				c.types.contains(OwlNode.NodeType.PROPERTY) ||
				c.types.contains(OwlNode.NodeType.NAMED_INDIVIDUAL)) {

			// skip bnodes
			if(c.uri == null)
				continue;

			for(String prop : synonymProperties) {
				List<PropertyValue> values = c.properties.getPropertyValues(prop);
				if(values != null) {
					for(PropertyValue value : values) {
						c.properties.addProperty("synonym", value);
					}
				}
			}
		    }
		}
		long endTime3 = System.nanoTime();
		System.out.println("annotate synonyms: " + ((endTime3 - startTime3) / 1000 / 1000 / 1000));


	}
}
