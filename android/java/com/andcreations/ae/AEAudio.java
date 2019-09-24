package com.andcreations.ae;

import java.util.List;
import java.util.ArrayList;
import java.io.IOException;

import android.app.Activity;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.MediaPlayer;
import android.media.AudioManager;
import android.media.SoundPool;
import android.media.SoundPool.OnLoadCompleteListener;

import com.andcreations.ae.Log;

/**
 * @author Mikolaj Gucki
 */
public class AEAudio {
    /** */
    private static class Sound {
        /** */
        private int id = -1;
        
        /** */
        private String filename;
        
        /** */
        private Sound(String filename) {
            this.filename = filename;
        }
    }
    
    /** */
    private static final String TAG = "AE/Audio";
    
    /** */
    private static final int MAX_SOUND_POOL_STREAMS = 16; 
    
    /** */
    private static final String MUSIC_DIR = "music";
    
    /** */
    private static final String SOUNDS_DIR = "sounds";
    
    /** The directory in the assets directory with music files. */
    private static String musicDir = MUSIC_DIR;
    
    /** The directory in the assets directory with sounds files. */
    private static String soundsDir = SOUNDS_DIR;
    
    /** */
    private static Activity activity;
    
    /** The current media player. */
    private static MediaPlayer player;
    
    /** The sound pool. */
    private static SoundPool soundPool;
    
    /** The loaded sounds. */
    private static List<Sound> sounds;
    
    /** The global volume. */
    private static double volume;
    
    /** The music volume. */
    private static double musicVolume;
    
    /** */
    static void onCreate(Activity activity) {
        Log.d(TAG,"onCreate()");
        AEAudio.activity = activity;
        init();
    }
    
    /** */
    private static void createSoundPool() {
        Log.d(TAG,"createSoundPool()");
        
        soundPool = new SoundPool(MAX_SOUND_POOL_STREAMS,
            AudioManager.STREAM_MUSIC,0);  
        soundPool.setOnLoadCompleteListener(new OnLoadCompleteListener() {
            /** */
            @Override
            public void onLoadComplete(SoundPool soundPool,int soundId,
                int status) {
            //
                if (status == 0) {
                    soundLoaded(soundId);
                }
                else {
                    failedToLoadSound(soundId,status);
                }
            }
        });        
    }
    
    /** */
    private static void deleteSoundPool() {
        soundPool.release();
        soundPool = null;
    }
    
    /** */
    private static void init() {
        Log.d(TAG,"init()");
        
    // let the volume up/down buttons control the media volume
        activity.setVolumeControlStream(AudioManager.STREAM_MUSIC);
        
    // delete media player
        if (player != null) {
            deleteMusic();
        }
        
    // delete sound pool
        if (soundPool != null) {
            deleteSoundPool();
        }
        
    // create sound pool
        createSoundPool();
        sounds = new ArrayList<Sound>();
        
    // global volume
        volume = 1.0;
    }
    
    /** */
    static void onPause() {
        /*
        Log.d(TAG,"onPause()");
        
        if (soundPool != null) {
            deleteSoundPool();
        }
        else {
            Log.d(TAG,"onPause(): sound pool does not exist!");
        }
        
        synchronized (sounds) {
            for (Sound sound:sounds) {
                sound.id = -1;
            }
            
        // in case a thread is waiting for a sound to load
            sounds.notify();
        }
        */
    }
    
    /** */
    private static void loadSounds() {
        Log.d(TAG,"loadSounds()");
        
        for (Sound sound:sounds) {
            try {
                loadSound(sound);
            } catch (IOException exception) {
                Log.e(TAG,"Failed to load sound " + sound.filename + ": " +
                    exception.getMessage());
            }
        }
    }
    
    /** */
    static void onResume() {
        /*
        Log.d(TAG,"onResume()");
        
        if (soundPool == null) {
            createSoundPool();
        }
        else {
            Log.d(TAG,"onResume(): sound pool exists!");
        }
        loadSounds();
        */
    }
    
    /** */
    private static Sound findSound(int soundId) {
        for (Sound sound:sounds) {
            if (sound.id == soundId) {
                return sound;
            }
        }
        
        return null;
    }
    
    /** */
    public static void loadMusic(String filename) throws IOException {
        filename = musicDir + "/" + filename;
        Log.d(TAG,"loadMusic(" + filename + ")");
        
    // player
        player = new MediaPlayer();
        player.setLooping(true);
       
    // source from file
        AssetFileDescriptor fd = activity.getAssets().openFd(filename);
        player.setDataSource(fd.getFileDescriptor(),
            fd.getStartOffset(),fd.getLength());
            
    // prepare
        player.prepare();
    }
    
    /** */
    public static void deleteMusic() {
        Log.d(TAG,"deleteMusic()");
        if (player == null) {
            return;
        }
        
        player.release();
        player = null;        
    }
    
    /** */
    public static void playMusic() {
        Log.d(TAG,"playMusic()");
        if (player == null) {
            Log.w(TAG,"No music loaded");
            return;
        }
        player.start();        
    }
    
    /** */
    public static void pauseMusic() {
        Log.d(TAG,"pauseMusic()");
        if (player == null) {
            Log.w(TAG,"No music loaded");
            return;
        }
        player.pause();        
    }
    
    /** */
    public static void resumeMusic() {
        Log.d(TAG,"resumeMusic()");
        if (player == null) {
            Log.w(TAG,"No music loaded");
            return;
        }
        player.start();        
    }
    
    /** */
    public static void stopMusic() {
        Log.d(TAG,"stopMusic()");
        if (player == null) {
            Log.w(TAG,"No music loaded");
            return;
        }
        player.stop();        
    }   
    
    /** */
    public static void setMusicVolume(double volume) {
        AEAudio.musicVolume = volume;
        updateMusicVolume();
    }
    
    /** */
    private static void updateMusicVolume() {
        if (player == null) {
            return;
        }
        
        float volume = (float)(AEAudio.musicVolume * AEAudio.volume); 
        player.setVolume(volume,volume);
    }
    
    /** */
    private static int loadSound(Sound sound) throws IOException {
        String path = soundsDir + "/" + sound.filename;
        Log.d(TAG,"loadSound(" + path + ")");
        
        int soundId = -1;
        synchronized (sounds) {
        // load 
            AssetFileDescriptor fd = activity.getAssets().openFd(path);
            soundId = soundPool.load(fd,1);
            Log.d(TAG,"Requested to load sound "  + path +
                " with identifier " + soundId); 
            
        // wait for the sound to load
            /*
            try {
                sounds.wait();
            } catch (InterruptedException exception) {
            }
            */
        }
        return soundId;        
    }
    
    /** */
    public static int loadSound(String filename) throws IOException {
        Sound sound = new Sound(filename);
        sounds.add(sound);
        return loadSound(sound);
    }
    
    /** */
    private static void soundLoaded(int soundId) {
        Log.d(TAG,"soundLoaded(" + soundId + ")");
        /*
        synchronized (sounds) {
            sounds.notify();
        }
        */
    }    
    
    /** */
    private static void failedToLoadSound(int soundId,int status) {
        Log.d(TAG,"failedToLoadSound(" + soundId + "," + status + ")");
    }
    
    /** */
    public static void deleteSound(int soundId) {
        Sound sound = findSound(soundId);
        if (sound == null) {
            return;
        }
        Log.d(TAG,"deleteSound(" + sound.filename + ")");
        
        if (soundPool != null) {
            soundPool.unload(soundId);
        }
        sounds.remove(sound);
    }
    
    /** */
    public static void playSound(int soundId) {
        if (soundPool == null) {
            Log.w(TAG,"No sound pool");
            return;
        }
        soundPool.play(soundId,
            (float)volume,(float)volume, // left, right volume
            0, // priority
            0, // loop
            1); // rate
    }
    
    /** */
    public static void setVolume(double volume) {
        AEAudio.volume = volume;
        
    // set sound volume
        if (sounds != null && soundPool != null) {
            for (Sound sound:sounds) {
                soundPool.setVolume(sound.id,(float)volume,(float)volume);
            }
        }
    }
}