/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tunnelsnake;

import java.applet.*;

/**
 *
 * @author Gry
 */
public class SoundEngine
{
   private String[] soundNameList = {"122659__acollier123__blip-no-sound-select.wav","122664__acollier123__flute-c-hi-short.wav","173859__jivatma07__j1game-over-mono.wav"};
   private String[] musicNameList = {   
                                        "202135__luckylittleraven__the-pact-full-version.wav",
                                        "182823__zagi2__chiptune-loop.wav", 
                                        "49685__ejfortin__nano-blade-loop.wav",
                                        "179287__erokia__velatrisx-ambient-loading-screen-music-3.wav",
                                        "174589__dingo1__down-dark-metal-electronic-loop.wav",
                                        "198896__bone666138__8-bit-circus-music.wav",
                                        "172561__djgriffin__video-game-7.wav"
                                    };
    
    private AudioClip[] soundList = new AudioClip[soundNameList.length];
    private AudioClip[] musicList = new AudioClip[musicNameList.length];

    
    public SoundEngine()
    {

        
        for(int i=0; i<soundList.length; i++)
        {
            try 
            {
                java.net.URL imgURL = getClass().getResource("sounds/"+soundNameList[i]);
                java.net.URL imgURL2 = getClass().getResource("sounds/music/"+musicNameList[i]);
                soundList[i] = Applet.newAudioClip(imgURL);
                musicList[i] = Applet.newAudioClip(imgURL2);
            } 
            catch (Exception e) 
            {
            }  
            
        }
        
                for(int i=0; i<musicList.length; i++)
        {
            try 
            {
                java.net.URL imgURL = getClass().getResource("sounds/music/"+musicNameList[i]);
                musicList[i] = Applet.newAudioClip(imgURL);
            } 
            catch (Exception e) 
            {
            }  
        }
    
    }
    
    
    public void playSound(int index)
    {
        soundList[index].play();   
    }
    public void stopSound(int index)
    {
        soundList[index].stop();
    }  
    public void loopSound(int index)
    {
        soundList[index].loop();  
    }
    
    public void stopAllSounds()
    {
        for(int i=0; i<soundList.length; i++)
            soundList[i].stop();
    }
    
    public void loopSong(int index)
    {
        musicList[index].loop();  
    }
        
    public void stopAllSongs()
    {
        for(int i=0; i<musicList.length; i++)
            musicList[i].stop();  
    }
    
    
    public int musicListLegth()
    {
        return musicList.length;
    } 
    
                
    
    
    
    
    
    
}
