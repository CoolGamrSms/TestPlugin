package com.aquanova_mp.Homes;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Created by Archer on 03-Apr-16.
 */
public class Import {
    private static int acc = 1000000;

    public static boolean essentials(Homes homes) {
        String path = homes.getDataFolder().getPath().replace(homes.getName(),"")+"Essentials"+File.separator+"userdata"+File.separator;
        homes.print(Messages.tag+"FOUND ESSENTIALS PATH: "+path);
        File homeDir = new File(path);

        File[] files = homeDir.listFiles();
        int counter = 1;
        int invalid = 0;
        boolean lockedOnHome;
        boolean badHome;
        for (File file : files) {
            lockedOnHome = false;

            String name = "";
            String id = FilenameUtils.getBaseName(file.getName());
            String world = "";
            double x = 0;
            double y = 0;
            double z = 0;
            float yaw = 0;
            float pitch = 0;


            if (FilenameUtils.getExtension(file.getName()).equalsIgnoreCase("yml")) {
                try {
                    Scanner sc = new Scanner(file);

                    int lineCounter = 0;
                    badHome = false;

                    while (sc.hasNext()) {

                        String line = sc.nextLine();
                        lineCounter++;

                        if (lineCounter==1 && !line.contains("lastAccountName:")) {
                            System.out.println("=======================================");
                            System.out.println("----------HOME: " + counter + "IS INVALID---------");
                            System.out.println("=======================================");
                            invalid++;
                            badHome = true;
                            break;
                        }

                        if (line.contains("lastAccountName:")) {
                            line = line.replace("lastAccountName:", "");
                            line = StringUtils.deleteWhitespace(line);
                            name = line;
                        }

                        if (line.contains("home:")) {
                            lockedOnHome = true;
                        }

                        if (lockedOnHome && line.contains("world:")) {
                            line = line.replace("world:", "");
                            line = StringUtils.deleteWhitespace(line);
                            world = line;
                        }
                        if (lockedOnHome && line.contains("x:")) {
                            line = line.replace("x:", "");
                            line = StringUtils.deleteWhitespace(line);
                            x = Math.floor(Double.parseDouble(line)*acc) / acc;
                        }
                        if (lockedOnHome && line.contains("y:")) {
                            line = line.replace("y:", "");
                            line = StringUtils.deleteWhitespace(line);
                            y = Math.floor(Double.parseDouble(line)*acc) / acc;
                        }
                        if (lockedOnHome && line.contains("z:")) {
                            line = line.replace("z:", "");
                            line = StringUtils.deleteWhitespace(line);
                            z = Math.floor(Double.parseDouble(line)*acc) / acc;
                        }
                        if (lockedOnHome && line.contains("yaw:")) {
                            line = line.replace("yaw:", "");
                            line = StringUtils.deleteWhitespace(line);
                            yaw = Float.parseFloat(line);
                        }
                        if (lockedOnHome && line.contains("pitch:")) {
                            line = line.replace("pitch:", "");
                            line = StringUtils.deleteWhitespace(line);
                            pitch = Float.parseFloat(line);
                            break;
                        }

                    }
                    if(!badHome) {
                        NameID nameId = new NameID(name, id);
                        HomeInfo homeInfo = new HomeInfo(nameId, world, x, y, z, yaw, pitch);
                        boolean isNew = homes.getData().add(homeInfo);

                        if(isNew) {
                            System.out.println("============HOME: " + counter + "===================");
                            System.out.println("ID: " + id);
                            System.out.println("NAME: " + name);
                            System.out.println(" WORLD: " + world);
                            System.out.println(" X: " + x);
                            System.out.println(" Y: " + y);
                            System.out.println(" Z: " + z);
                            System.out.println(" YAW: " + yaw);
                            System.out.println(" PITCH: " + pitch);

                            counter++;
                        }
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }

        if (counter == 1)
            homes.print(Messages.HOME_IMPORT_NONE.parse());
        else
            homes.print(Messages.HOME_IMPORT_COUNT.parse(counter));
        homes.print(Messages.HOME_IMPORT_INVALID.parse(invalid));

        return true;
    }

}
