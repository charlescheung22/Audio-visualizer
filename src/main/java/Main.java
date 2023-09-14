import javax.sound.sampled.*;
import javax.swing.*;





public class Main {


    /**
     * Gets the primary sound driver mixer info.
     * @return The primary sound driver mixer info.
     */
    private static Mixer.Info getPrimarySoundDriverMixerInfo() {
        // I'm pretty sure this is only for Windows. Sucks to be using others ;)
        Mixer.Info[] mixerInfos = AudioSystem.getMixerInfo();
        for (Mixer.Info info : mixerInfos) {
            if (info.getName().contains("Primary Sound Driver") ||
                    info.getDescription().contains("Primary Sound Driver")) {
                return info;
            }
        }
        return null; // TODO use Optional.of to avoid null
    }

    /**
     * Gets the default audio format for Windows.
     * @return The default audio format for Windows.
     */
    static AudioFormat getWindowsDefaultAudioFormat() {
        float sampleRate = 48000;
        int sampleSizeInBits = 24;
        int channels = 2;
        boolean signed = true;
        boolean bigEndian = true;
        return new AudioFormat(sampleRate, sampleSizeInBits,
                channels, signed, bigEndian);
    }



    public static void main(String[] args) {
        Mixer.Info mixerInfo = getPrimarySoundDriverMixerInfo();  // HELP TODO None of the available mixers have target data lines at all????

        if (mixerInfo == null) {  // TODO: consider using Optionals
            throw new IllegalArgumentException("No primary sound driver mixer info found");
        }

        AudioFormat format = getWindowsDefaultAudioFormat();
        Mixer mixer = AudioSystem.getMixer(mixerInfo);
        DataLine.Info info = new DataLine.Info(TargetDataLine.class, format); // format is an AudioFormat object
        if (!mixer.isLineSupported(info)) {
            throw new IllegalArgumentException("Line not supported");
        }

        // Obtain and open the line.
        try {
            TargetDataLine line = (TargetDataLine) mixer.getLine(info);  // safe cast, checked for TargetDataLine above
            line.open(format);
        } catch (LineUnavailableException lue) {
            throw new RuntimeException(lue);
        }

    }

}
