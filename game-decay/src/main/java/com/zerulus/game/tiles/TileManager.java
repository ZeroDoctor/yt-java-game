package com.zerulus.game.tiles;

import com.zerulus.game.entity.material.MaterialManager;
import com.zerulus.game.graphics.SpriteSheet;
import com.zerulus.game.tiles.blocks.NormBlock;
import com.zerulus.game.util.Camera;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.io.File;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class TileManager {

    public static ArrayList<TileMap> tm;
    public Camera cam;
    private SpriteSheet spritesheet;
    private int blockWidth;
    private int blockHeight;
    private MaterialManager mm;
    

    public TileManager() {
        tm = new ArrayList<TileMap>();
    }

    public TileManager(String path, Camera cam) {
        this();
        addTileMap(path, 64, 64, cam);
    }

    public TileManager(String path, int blockWidth, int blockHeight, Camera cam) {
        this();
        addTileMap(path, blockWidth, blockHeight, cam);
    }

    public TileManager(SpriteSheet spritesheet, int chuckSize, Camera cam, MaterialManager mm) { 
        this();
        addTileMap(spritesheet, 64, 64, chuckSize, cam, mm);

    }

    public TileManager(SpriteSheet spritesheet, int blockWidth, int blockHeight, int chuckSize, Camera cam, MaterialManager mm) {
        this();
        addTileMap(spritesheet, blockWidth, blockHeight, chuckSize, cam, mm);
        System.gc();
    }

    public void generateTileMap(int chuckSize) {
        addTileMap(spritesheet, blockWidth, blockHeight, chuckSize, cam, mm);
    }

    
    private void addTileMap(SpriteSheet spritesheet, int blockWidth, int blockHeight, int chuckSize, Camera cam, MaterialManager mm) {
        this.cam = cam;
        this.mm = mm;
        this.spritesheet = spritesheet;
        this.blockWidth =  blockWidth;
        this.blockHeight = blockHeight;

        cam.setTileSize(blockWidth);
        String[] data = new String[3];
        TileMapGenerator tmg = new TileMapGenerator(chuckSize, blockWidth, mm);
        
        // For now
        data[0] = "";

        for(int i = 0; i < chuckSize; i++){
            for(int j = 0; j < chuckSize; j++){
                data[0] += "0,";
            }
        }

        tm.add(new TileMapObj(data[0], spritesheet, chuckSize, chuckSize, blockWidth, blockHeight, 9));

        tm.add(new TileMapNorm(tmg.base, spritesheet, chuckSize, chuckSize, blockWidth, blockHeight, 9));
        tm.add(new TileMapNorm(tmg.onTop, spritesheet, chuckSize, chuckSize, blockWidth, blockHeight, 9));

        cam.setLimit(chuckSize * blockWidth, chuckSize * blockHeight);

    }

    private void addTileMap(String path, int blockWidth, int blockHeight, Camera cam) {
        this.cam = cam;
        cam.setTileSize(blockWidth);
        String imagePath;

        int width = 0;
        int height = 0;
        int tileWidth;
        int tileHeight;
        int tileColumns;
        int layers = 0;
        SpriteSheet sprite;

        String[] data = new String[10];

        try {
            DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = builderFactory.newDocumentBuilder();
            Document doc = builder.parse(new File(getClass().getClassLoader().getResource(path).toURI()));
            doc.getDocumentElement().normalize();

            NodeList list = doc.getElementsByTagName("tileset");
            Node node = list.item(0);
            Element eElement = (Element) node;

            imagePath = eElement.getAttribute("name");
            tileWidth = Integer.parseInt(eElement.getAttribute("tilewidth"));
            tileHeight = Integer.parseInt(eElement.getAttribute("tileheight"));
            tileColumns =  Integer.parseInt(eElement.getAttribute("columns"));
            
            sprite = new SpriteSheet("tile/" + imagePath + ".png", tileWidth, tileHeight);

            list = doc.getElementsByTagName("layer");
            layers = list.getLength();

            for(int i = 0; i < layers; i++) {
                node = list.item(i);
                eElement = (Element) node;
                if(i <= 0) {
                    width = Integer.parseInt(eElement.getAttribute("width"));
                    height = Integer.parseInt(eElement.getAttribute("height"));
                }

                data[i] = eElement.getElementsByTagName("data").item(0).getTextContent();

                if(i >= 1) {
                    tm.add(new TileMapNorm(data[i], sprite, width, height, blockWidth, blockHeight, tileColumns));
                } else {
                    tm.add(new TileMapObj(data[i], sprite, width, height, blockWidth, blockHeight, tileColumns));
                }
            }

            cam.setLimit(width * blockWidth, height * blockHeight);
        } catch(Exception e) {
            System.out.println("ERROR - TILEMANAGER: can not read tilemap:");
            e.printStackTrace();
            System.exit(0);
        }
    }

    public NormBlock getNormalTile(int id) {
        int normMap = 1;
        if(tm.size() < 2) normMap = 0; 
        NormBlock block = (NormBlock) tm.get(normMap).getBlocks()[id];
        if(block != null) {
            return block;
        }

        return null;
    }

    public void render(Graphics2D g) {
        if(cam == null)
            return;
            
        for(int i = 0; i < tm.size(); i++) {
            tm.get(i).render(g, cam.getBounds());
        }
    }
}
