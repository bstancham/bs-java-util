package info.bstancham.bsjavautil.misc;

/**
 * <p>Generates random names, which are usually pronouncible, via a very simple
 * algorithm.</p>
 */
public class RandomNameGenerator {

    private static final String[] vowels = {"a","e","i","o","u"};
    private static final String[] consonants = {
        "b","c","d","f","g","h","j","k","l","m",
        "n","p","q","r","s","t","v","w","x","y","z"};

    /**
     * @return A random name of length {@code len}.
     */
    public static String randomName(int len) {
        String word = "";
        int i = 0;
        while (i < len) {
            if (Math.random() < 0.5) {
                word += vowels[(int)(Math.random() * vowels.length)];
            } else {
                word += consonants[(int)(Math.random() * consonants.length)];
            }
		
            // capitalize first letter
            if (i == 0) word = word.toUpperCase();
		
            i++;
        }
        return word;
    }
    
}
