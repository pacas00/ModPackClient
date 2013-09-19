package net.petercashel.client;

import java.io.File;

public class MC_Util {

   public static final String APPLICATION_NAME = "minecraft";


   public static MC_Util.OS getPlatform() {
      String osName = System.getProperty("os.name").toLowerCase();
      return osName.contains("win")?MC_Util.OS.WINDOWS:(osName.contains("mac")?MC_Util.OS.MACOS:(osName.contains("linux")?MC_Util.OS.LINUX:(osName.contains("unix")?MC_Util.OS.LINUX:MC_Util.OS.UNKNOWN)));
   }

   public static File getWorkingDirectory() {
      String userHome = System.getProperty("user.home", ".");
      File workingDirectory;
      switch(MC_Util.NamelessClass939304230.$SwitchMap$net$minecraft$bootstrap$MC_Util$OS[getPlatform().ordinal()]) {
      case 1:
      case 2:
         workingDirectory = new File(userHome, ".minecraft/");
         break;
      case 3:
         String applicationData = System.getenv("APPDATA");
         String folder = applicationData != null?applicationData:userHome;
         workingDirectory = new File(folder, ".minecraft/");
         break;
      case 4:
         workingDirectory = new File(userHome, "Library/Application Support/minecraft");
         break;
      default:
         workingDirectory = new File(userHome, "minecraft/");
      }

      return workingDirectory;
   }

   // $FF: synthetic class
   static class NamelessClass939304230 {

      // $FF: synthetic field
      static final int[] $SwitchMap$net$minecraft$bootstrap$MC_Util$OS = new int[MC_Util.OS.values().length];


      static {
         try {
            $SwitchMap$net$minecraft$bootstrap$MC_Util$OS[MC_Util.OS.LINUX.ordinal()] = 1;
         } catch (NoSuchFieldError var4) {
            ;
         }

         try {
            $SwitchMap$net$minecraft$bootstrap$MC_Util$OS[MC_Util.OS.SOLARIS.ordinal()] = 2;
         } catch (NoSuchFieldError var3) {
            ;
         }

         try {
            $SwitchMap$net$minecraft$bootstrap$MC_Util$OS[MC_Util.OS.WINDOWS.ordinal()] = 3;
         } catch (NoSuchFieldError var2) {
            ;
         }

         try {
            $SwitchMap$net$minecraft$bootstrap$MC_Util$OS[MC_Util.OS.MACOS.ordinal()] = 4;
         } catch (NoSuchFieldError var1) {
            ;
         }

      }
   }

   public static enum OS {

      WINDOWS("WINDOWS", 0),
      MACOS("MACOS", 1),
      SOLARIS("SOLARIS", 2),
      LINUX("LINUX", 3),
      UNKNOWN("UNKNOWN", 4);
      // $FF: synthetic field
      private static final MC_Util.OS[] $VALUES = new MC_Util.OS[]{WINDOWS, MACOS, SOLARIS, LINUX, UNKNOWN};


      private OS(String var1, int var2) {}

   }
}