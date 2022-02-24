package Chess;
import java.applet.AudioClip;
import java.net.MalformedURLException;
import java.net.URL;
import javax.swing.JApplet;
public class Music {
	static AudioClip music;
	public static AudioClip loadSound(String filename) {
		URL url=null;
		try {
			url=new URL("file:"+filename);
		}
		catch(MalformedURLException e) {;
		}
		return JApplet.newAudioClip(url);
	}
	public static void play1() {
		music=loadSound("music/music1.wav");
		music.loop();
	}
	public static void play2() {
		music=loadSound("music/music2.wav");
		music.loop();
	}
	public static void stop() {
		music.stop();
	}
}
