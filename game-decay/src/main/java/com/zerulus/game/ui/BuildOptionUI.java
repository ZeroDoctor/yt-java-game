package com.zerulus.game.ui;

import com.zerulus.game.GamePanel;
import com.zerulus.game.graphics.SpriteSheet;
import com.zerulus.game.math.Vector2f;
import com.zerulus.game.states.GameStateManager;

import java.awt.image.BufferedImage;

public class BuildOptionUI {

    private SpriteSheet slots;
    private SpriteSheet icons;

    private BufferedImage imgButton;
    private BufferedImage imgPressed;

    private Slots[] parentSlots;

    public BuildOptionUI() {
        this.slots = new SpriteSheet("ui/slots.png");
        this.icons = new SpriteSheet("ui/icons.png", 32, 32);

        this.imgButton = GameStateManager.button.getSubimage(0, 137, 40, 40);
        this.imgPressed = GameStateManager.button.getSubimage(41, 137, 40, 40);

        this.parentSlots = createBuildingUI();
    }

    public Slots[] getSlots() {
        return parentSlots;
    }


    private Slots[] createGather(int size, int x, int y, BufferedImage[] slotsHori) {
        Button[] btnOptions = new Button[2];
        for(int i = 0; i < btnOptions.length; i++) {
            btnOptions[i] = new Button(icons.getSprite(i, 1).image, imgButton, new Vector2f(x - (size + 10) * (i + 1), y), size, size, 8);
            btnOptions[i].addPressedImage(imgPressed);
        }

        Vector2f[][] optionsPos = new Vector2f[btnOptions.length][slotsHori.length];

        int img = 0;
        for(int i = 0; i < btnOptions.length; i++) {
            for(int j = 0; j < optionsPos[0].length; j++) {
                img = (i == btnOptions.length - 1 && j == btnOptions.length - 1) ? 0 : 1;
                optionsPos[i][j] = new Vector2f(
                x - ((size + 10) * (i + 1)) + ((size + 12) * ((j + 1) % 2)) - slotsHori[img].getWidth(),
                y + ((size + 8) - slotsHori[img].getHeight()) / 2);
            }
        }

        Slots[] optionslots = new Slots[btnOptions.length];
        optionslots[0] = new Slots(btnOptions[0], new BufferedImage[] { null, slotsHori[1], slotsHori[1] }, optionsPos[0], 0);
        optionslots[1] = new Slots(btnOptions[1], new BufferedImage[] { null, slotsHori[1], slotsHori[0] }, optionsPos[1], 0);

        return optionslots;
    }

    private Slots[] createBuildingUI() {
        SpriteSheet slots = new SpriteSheet("ui/slots.png");
        SpriteSheet icons = new SpriteSheet("ui/icons.png", 32, 32);

        BufferedImage imgButton = GameStateManager.button.getSubimage(0, 137, 40, 40);
        BufferedImage imgPressed = GameStateManager.button.getSubimage(41, 137, 40, 40);

        BufferedImage slotsHori[] = {
            slots.getSubimage(409, 80, 14, 24), // ends
            slots.getSubimage(389, 76, 4, 32) // chain
        };

        int size = 40;
        int buildSlotX = GamePanel.width - size - 15;
        int buildSlotY = GamePanel.height / 3;

        Slots[] gatherSlots = createGather(size, buildSlotX, buildSlotY, slotsHori);

        BufferedImage slotsVert[] = {
            slots.getSubimage(393, 56, 24, 14), // ends
            slots.getSubimage(389, 71, 32, 4) // chain
        };

        Button[] btnBuilding = new Button[3];
        for (int i = 0; i < btnBuilding.length; i++) {
            btnBuilding[i] = new Button(icons.getSprite(i, 0).image, imgButton,
                    new Vector2f(buildSlotX, buildSlotY + ((size + 10) * i)), size, size, 8);
            btnBuilding[i].addPressedImage(imgPressed);
        }

        Vector2f[][] slotsPos = new Vector2f[btnBuilding.length][slotsVert.length];

        int img = 0;
        for (int i = 0; i < btnBuilding.length; i++) {
            for (int j = 0; j < slotsPos[0].length; j++) {
                img  = i > 0 ? 1 : j;
                slotsPos[i][j] = new Vector2f(
                    buildSlotX + ((size + 8) - slotsVert[img].getWidth()) / 2, 
                    buildSlotY + ((size + 10) * i) + ((size + 18) * (j % 2)) - slotsVert[img].getHeight());
            }
        }

        Slots[] buildingslots = new Slots[btnBuilding.length]; // probably make this a for loop later...
        buildingslots[0] = new Slots(btnBuilding[0], gatherSlots, new BufferedImage[] { null, slotsVert[0], slotsVert[1] }, slotsPos[0], 0);
        buildingslots[1] = new Slots(btnBuilding[1], new BufferedImage[] { null, slotsVert[1], slotsVert[1] }, slotsPos[1], 0);
        buildingslots[2] = new Slots(btnBuilding[2], new BufferedImage[] { null, slotsVert[1], null }, slotsPos[2], 0);

        return buildingslots;
    }



}