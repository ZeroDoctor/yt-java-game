package com.zerulus.game.states.levels;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import com.zerulus.game.tiles.TileManager;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class Save {

    public Save(TileManager tm) {
        System.out.println("Saving map...");
        saveMap(tm);
        
    }

    private void saveMap(TileManager tm) {
        try {

            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            Document doc = docBuilder.newDocument();
            Element rootElement = doc.createElement("map");

            Attr width = doc.createAttribute("width");
            width.setValue(Integer.toString(tm.getChunkSize()));
            rootElement.setAttributeNode(width);

            Attr height = doc.createAttribute("height");
            height.setValue(Integer.toString(tm.getChunkSize()));
            rootElement.setAttributeNode(height);

            Attr tilewidth = doc.createAttribute("tilewidth");
            tilewidth.setValue(Integer.toString(tm.getBlockWidth()));
            rootElement.setAttributeNode(tilewidth);

            Attr tileheight = doc.createAttribute("tileheight");
            tileheight.setValue(Integer.toString(tm.getBlockHeight()));
            rootElement.setAttributeNode(tileheight);

            doc.appendChild(rootElement);

            Element tileset = doc.createElement("tileset");
            Attr name = doc.createAttribute("name");
            name.setValue(tm.getFilename());
            tileset.setAttributeNode(name);

            Attr columns = doc.createAttribute("columns");
            columns.setValue(Integer.toString(tm.getColumns()));
            tileset.setAttributeNode(columns);
            tileset.setAttributeNode(tilewidth);
            tileset.setAttributeNode(tileheight);

            rootElement.appendChild(tileset);

            // create function for layers?

            Element solid = doc.createElement("data");
            Attr nameSolid = doc.createAttribute("name");
            nameSolid.setValue("Solid");
            solid.setAttributeNode(nameSolid);
            solid.setAttributeNode(width);
            solid.setAttributeNode(height);

            Element data = doc.createElement("data");
            data.appendChild(doc.createTextNode(tm.getSolid()));
            solid.appendChild(data);

            rootElement.appendChild(solid);

            Element layer1 = doc.createElement("data");
            Attr nameLayer1 = doc.createAttribute("name");
            nameLayer1.setValue("Layer1");
            layer1.setAttributeNode(nameLayer1);
            layer1.setAttributeNode(width);
            layer1.setAttributeNode(height);

            Element data1 = doc.createElement("data");
            data1.appendChild(doc.createTextNode(tm.getGenMap()));
            layer1.appendChild(data1);

            rootElement.appendChild(layer1);

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File("C:\\file.xml"));

            transformer.transform(source, result);
            System.out.println("Map saved!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}