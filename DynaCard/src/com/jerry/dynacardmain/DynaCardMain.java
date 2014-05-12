/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jerry.dynacardmain;

import java.net.URL;
import java.util.Scanner;
import com.jerry.dynacard.DynaCard;
import java.io.FileNotFoundException;

/**
 *
 * @author jerry
 */
public class DynaCardMain {

    /**
     * @param args the command line arguments
     */
    
    private static String GetCardText(String cardName) throws FileNotFoundException
    {
        String text = null;
                
          try {
            final String resourcesPath = cardName;
            URL xmlUrl = DynaCardMain.class.getResource(resourcesPath);
            text = new Scanner(DynaCardMain.class.getResourceAsStream(resourcesPath), "UTF-8").useDelimiter("\\A").next();
        } catch (Exception ex) {
            throw new FileNotFoundException();
        }   
          
        return text;
    }
    
    public static void main(String[] args) {

        String xmlText;
        String cardName = "ShutDownCard.xml1";
        
        try {
            String error = null;
            xmlText = GetCardText(cardName);
        }
        catch (FileNotFoundException ex)
        {
            System.out.println("Failed to load card text: " + cardName);
            return;
        }
            
        DynaCard card = new DynaCard(xmlText);
        try {
            card.Load();
        } catch (Exception ex) {
            System.out.println("Exception" + ex.toString());
        }

        System.out.println("public static int getLength() { return " + Integer.toString(card.SurfacePoints.size()) + ";}");

        StringBuilder line = new StringBuilder();
        for (DynaCard.LoadPosPair pair : card.SurfacePoints) {
            if (line.length() == 0) {
                line.append("static float []  xVals = { ");
            } else {
                line.append(", ");
            }
            line.append(pair.pos).append("f");
        }
        line.append("};");
        System.out.println(line.toString());

        line = new StringBuilder();
        for (DynaCard.LoadPosPair pair : card.SurfacePoints) {
            if (line.length() == 0) {
                line.append("static float []  yVals = { ");
            } else {
                line.append(", ");
            }
            line.append(pair.load).append("f");
        }
        line.append("};");
        System.out.println(line.toString());

        int i=0;
        for (DynaCard.LoadPosPair pair : card.SurfacePoints) {
            line = new StringBuilder();
            line.append(i++).append("\t");
            line.append(pair.pos).append("\t");
            line.append(pair.load);
            System.out.println(line.toString());
        }
    }

}
