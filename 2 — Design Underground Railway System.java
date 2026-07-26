class UndergroundSystem {
    class CheckInInfo {
        String station;
        int time;
        CheckInInfo(String station, int time) {
            this.station = station;
            this.time = time;
        }
    }
    
    class TripStats {
        double totalTime;
        int count;
        TripStats(double totalTime, int count) {
            this.totalTime = totalTime;
            this.count = count;
        }
    }
    
    private Map<Integer, CheckInInfo> checkInMap;
    private Map<String, TripStats> tripStats;

    public UndergroundSystem() {
        checkInMap = new HashMap<>();
        tripStats = new HashMap<>();
    }
    
    public void checkIn(int id, String stationName, int t) {
        checkInMap.put(id, new CheckInInfo(stationName, t));
    }
    
    public void checkOut(int id, String stationName, int t) {
        CheckInInfo info = checkInMap.get(id);
        String route = info.station + "->" + stationName;
        double time = t - info.time;
        
        TripStats stats = tripStats.getOrDefault(route, new TripStats(0.0, 0));
        stats.totalTime += time;
        stats.count += 1;
        tripStats.put(route, stats);
        
        checkInMap.remove(id);
    }
    
    public double getAverageTime(String startStation, String endStation) {
        String route = startStation + "->" + endStation;
        TripStats stats = tripStats.get(route);
        return stats.totalTime / stats.count;
    }
}

