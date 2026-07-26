class MyHashSet {
    private static final int BUCKET_COUNT = 769; // prime number for better distribution
    private List<Integer>[] buckets;

    public MyHashSet() {
        buckets = new List[BUCKET_COUNT];
        for (int i = 0; i < BUCKET_COUNT; i++) {
            buckets[i] = new LinkedList<>();
        }
    }
    
    private int hash(int key) {
        return key % BUCKET_COUNT;
    }
    
    public void add(int key) {
        int idx = hash(key);
        if (!buckets[idx].contains(key)) {
            buckets[idx].add(key);
        }
    }
    
    public void remove(int key) {
        int idx = hash(key);
        buckets[idx].remove(Integer.valueOf(key)); // remove by object, not index
    }
    
    public boolean contains(int key) {
        int idx = hash(key);
        return buckets[idx].contains(key);
    }
}

