class Solution {
    public boolean isAnagram(String s, String t) {

        // Check if both strings have the same length
        if (s.length() != t.length()) {
            return false;
        }

        // Convert strings to character arrays
        char[] first = s.toCharArray();
        char[] second = t.toCharArray();

        // Sort both arrays
        Arrays.sort(first);
        Arrays.sort(second);

        // Compare the sorted arrays
        return Arrays.equals(first, second);
    }
}
 
