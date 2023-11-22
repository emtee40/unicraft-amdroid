package com.rk.unicraft.world.generator;


import com.rk.unicraft.util.SimpleVector2;
import com.rk.unicraft.world.block.BlockFormation;
import com.rk.unicraft.world.block.Blocks;

import it.unimi.dsi.util.XoRoShiRo128PlusPlusRandom;
import java.util.Random;

public class TreeGenerator {

    int[][][] oakTreeFormation;
    int[][][] cactusFormation;

    private final XoRoShiRo128PlusPlusRandom random;  // Change to your custom random class
    private Random rand;
    public TreeGenerator(XoRoShiRo128PlusPlusRandom random) {
        this.random = random;
        this.rand = new Random();
    }

    public BlockFormation getTree(int type) {
        int[][][] formation;
        SimpleVector2 adj = new SimpleVector2();
        switch (type) {
            case 1:
                formation = mkOakTree();
                adj.set(-2, -2);
                break;
            case 10:
                formation = mkCactus();
                adj.set(0, 0);
                break;
            default:
                return null;
        }

        return new BlockFormation(formation, BlockFormation.MergeMode.ADD, adj);
    }

    private int[][][] mkOakTree() {
        if(oakTreeFormation == null) {
            oakTreeFormation = new int[5][8][5];

            //Blätter
            for (int i = 0; i < 5; i++) {
                for (int j = 3; j < 6; j++) {
                    for (int k = 0; k < 5; k++) {
                        oakTreeFormation[i][j][k] = Blocks.LEAF;
                    }
                }
            }

            //Blattkrone

            oakTreeFormation[1][6][1] = Blocks.LEAF;
            oakTreeFormation[1][6][2] = Blocks.LEAF;
            oakTreeFormation[1][6][3] = Blocks.LEAF;
            oakTreeFormation[2][6][1] = Blocks.LEAF;
            oakTreeFormation[2][6][2] = Blocks.LEAF;
            oakTreeFormation[2][6][3] = Blocks.LEAF;
            oakTreeFormation[3][6][1] = Blocks.LEAF;
            oakTreeFormation[3][6][2] = Blocks.LEAF;
            oakTreeFormation[3][6][3] = Blocks.LEAF;
            oakTreeFormation[2][7][2] = Blocks.LEAF;
            oakTreeFormation[1][7][2] = Blocks.LEAF;
            oakTreeFormation[3][7][2] = Blocks.LEAF;
            oakTreeFormation[2][7][1] = Blocks.LEAF;
            oakTreeFormation[2][7][3] = Blocks.LEAF;

            //Stamm
            oakTreeFormation[2][0][2] = Blocks.WOOD;
            oakTreeFormation[2][1][2] = Blocks.WOOD;
            oakTreeFormation[2][2][2] = Blocks.WOOD;
            oakTreeFormation[2][3][2] = Blocks.WOOD;
            oakTreeFormation[2][4][2] = Blocks.WOOD;
            oakTreeFormation[2][5][2] = Blocks.WOOD;
        }

        return oakTreeFormation;
    }

    private int[][][] mkCactus() {
        if(cactusFormation == null) {
            cactusFormation = new int[1][5][1];
            
            int height = rand.nextInt(3) + 3;

            for (int i = 0; i < height; i++) {
                cactusFormation[0][i][0] = Blocks.CACTUS;
            }
        }

        return cactusFormation;
    }
}
