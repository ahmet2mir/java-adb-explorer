/*
		Author : Ahmet DEMIR
		Version 1.0
		Date : May 2011
		Description : Java ADB Explorer allows you to explore your Anroid Phone.
		under License GPL: http://www.gnu.org/copyleft/gpl.html
		-----------------------------------------------------------
		Copyright (C) 2011 Ahmet DEMIR

 		This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.

 */

package adbexplorer.util;

public class ADBCommand {
	
	private ADBLogger log = new ADBLogger(ADBCommand.class);
	
	private Runtime runtime;
	private String device;
	
	/**
	 * Default constructor, just get the runtime
	 */
	public ADBCommand() {
		runtime = Runtime.getRuntime();
	}
	
	/**
	 * Constructor with device initialization
	 * @param device attached device
	 */
	public ADBCommand(String device) {
		this();
		this.device = device;
	}
	
	/**
	 * Set device
	 * @param device
	 */
	public void setDevice(String device) {
		this.device = device;
	}
	
	/**
	 * Execute a command in a shell
	 * @param command command to execute
	 * @return the return of the command
	 */
	public String exec(String command) {
		try {
			Process p = runtime.exec("adb -s "+device+" shell "+command);
			log.info("adb -s "+device+" shell "+command);
			
			java.io.DataInputStream in = new java.io.DataInputStream(p.getInputStream());
			
			byte[] buf = new byte[1024];
			int len = in.read(buf);
			if(len > 0)
				return new String(buf,0,len);
		}
		catch (java.io.IOException e) { log.error(e); }
		
		return "";
	}
	
	public String customExec(String cmd) {
		try {
			cmd = "adb -s " + device + " " + cmd;
			Process p = runtime.exec(cmd);
			log.info(cmd);
			//java.io.DataInputStream in = new java.io.DataInputStream(p.getInputStream());
			// If data received, return false because a success return nothing
			
			java.io.BufferedReader in = new java.io.BufferedReader(new java.io.InputStreamReader(p.getInputStream())); 
			String line = "";
			
			while((line = in.readLine()) != null) {
				System.out.println(line);
			}
			/*
			byte[] buf = new byte[1024];
			int len = in.read(buf);
			log.info("Size "+len);
			if(len > 0) return new String(buf, 0, len);*/
			
		}
		catch (java.io.IOException e) { 
			log.error(e);
		}	
		return "";
	}
	public String copyToLocal(String src, String dest) {
		String cmd = "pull "+src + " " + dest+"";
		return customExec(cmd);
	}
	
	public String copyToRemote(String src, String dest) {
		String cmd = "push "+src + " " + dest;
		return customExec(cmd);
	}
	
	public boolean rename(String oldName, String newName) {
		String cmd = "shell mv "+oldName + " " + newName;
		if(!customExec(cmd).isEmpty()) return true;
		else return false;
	}
	
	public boolean rm(String file) {
		String cmd = "shell rm "+file;
		if(!customExec(cmd).isEmpty()) return true;
		else return false;
	}
	
	public boolean rmdir(String file) {
		String cmd = "shell rmdir "+file;
		if(!customExec(cmd).isEmpty()) return true;
		else return false;
	}
	
	public java.util.Vector<adbexplorer.util.FileType> showDirectoryContent(String directory) {
		
		java.util.Vector<adbexplorer.util.FileType> obj = null;
		
		try {
			Process p = runtime.exec("adb -s "+device+" shell ls -l "+directory);
			//java.io.DataInputStream in = new java.io.DataInputStream(p.getInputStream());
			java.io.BufferedReader in = new java.io.BufferedReader(new java.io.InputStreamReader(p.getInputStream())); 
			String line = "";
			
			while((line = in.readLine()) != null) {
				if( obj == null ) obj = new java.util.Vector<adbexplorer.util.FileType>();
				
				String [] ligneContent = line.split(" ");
				String name="", path ="", permissions="";
				int type=0;
				if(ligneContent.length >= 5) { // If more than 5 columns
					
					
					name = ligneContent[ligneContent.length - 1];
					permissions = ligneContent[0].substring(1, ligneContent[0].length());
					path = directory.equals("/") || directory.isEmpty()? "/" + name: directory+ "/" + name;
					path = path.replace("//", "/");
					
					// If is directory
					if(ligneContent[0].substring(0, 1).equals("d")) {
						type = 1;
					}
					// If is a symbolic link
					else if(ligneContent[0].substring(0, 1).equals("l")) {
						// TODO Check if the symlink link to a file or a directory
						type = 2;
						name = ligneContent[ligneContent.length - 3];
						path = directory.equals("/") || directory.isEmpty()? "/" + name+"/": directory+ "/" + name +"/";
						path = path.replace("//", "/");
					}
					// Other i.e. a file
					else {
						type = 0;
					}
					adbexplorer.util.FileType adb = new FileType(name, path, type, permissions);
					obj.add(adb);
				}
			}
		}
		catch (java.io.IOException e) { 
			log.error(e);
		}
		return obj;
	}
	
	public String[] getDevices() {
		
		try {
			Process p = runtime.exec("adb devices");
			java.io.DataInputStream in = new java.io.DataInputStream(p.getInputStream());
			
			byte[] buf = new byte[1024];
			int len = in.read(buf);
			
			String[] ligne = new String(buf,0,len).split("\n");
			String[] retour = new String[ligne.length - 1]; // We don't take the first line "List of devices attached"
			
			for(int i=1; i<ligne.length; i++) {
				retour[i-1] = ligne[i].split("\t")[0];
			}
			
			return retour;
		}
		catch (java.io.IOException e) { log.error(e); }
		
		return null;
	}
	
	@Override
	public String toString() {
		return "ADBCommand [device=" + device + "]";
	}
	
	public static void main(String[] args) {
		ADBCommand adb = new ADBCommand("HT0BHRX12402");
		
		System.out.println("");
		System.out.println("$ " + adb.customExec("push /home/ademir/example.log /sdcard/example.log"));
		
		System.out.println("");
		java.util.Vector<adbexplorer.util.FileType> objList = adb.showDirectoryContent("/mnt");
		for(adbexplorer.util.FileType obj : objList)
			System.out.println("# " + obj.toFullString());
	}
	
	
	
	
}