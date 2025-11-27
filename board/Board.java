package board;

// manages game world grid with composite pattern, treating tiles uniformly
// generate() createss the world with pathfinding validation
// moveParty() handles navigation
// checkNecounter() triggers random battles, ensures playable map with accessible markets

import ui.GameUI;
import markets.Market;
import pieces.parties.Party;
import services.FileParserService;

import java.util.*;

public class Board {
    private final int w,h;
    private Tile[][] tiles;
    public int partyX, partyY;
    private final Party party;
    private final GameUI ui;
    private final FileParserService parser;
    private Random rng = new Random();

    public Board(int w, int h, Party p, GameUI ui, FileParserService parser) {
        this.w=w; 
        this.h=h; 
        this.party=p; 
        this.ui=ui; 
        this.parser=parser; 
        tiles=new Tile[h][w];
    }

    public void generate(boolean ensurePath) {
        for (int y=0;y<h;y++){
            for (int x=0;x<w;x++){
                tiles[y][x] = new Tile();
            }
        }

        int total = w * h; 
        int inac = Math.max(1, total*15/100); 
        int markets = Math.max(1, total*25/100);

        Set<Integer> used = new HashSet<Integer>();
        while (used.size() < inac){
            used.add(rng.nextInt(total));
        }

        for (int idx: used){ 
            tiles[idx/w][idx%w].setType(TileType.INACCESSIBLE); 
        }

        int placed = 0; 
        while (placed < markets) { 
            int idx = rng.nextInt(total); 
            int y = idx/w;
            int x = idx%w; 
            if (tiles[y][x].getType() == TileType.COMMON) { 
                tiles[y][x].setType(TileType.MARKET); 
                tiles[y][x].setMarket(Market.randomMarket(parser)); 
                placed++; 
            }
        }

        // pick start on a non inaccessible tile
        List<int[]> commons = new ArrayList<int[]>(); 
        for (int yy = 0; yy < h; yy++){
            for (int xx = 0; xx < w; xx++){
                if (tiles[yy][xx].getType() != TileType.INACCESSIBLE){
                    commons.add(new int[]{xx, yy});
                }
            }
        }

        if (commons.isEmpty()){ 
            partyX = 0; 
            partyY = 0; 
            tiles[0][0].setType(TileType.COMMON); 
        }
        else {
            int[] s = commons.get(rng.nextInt(commons.size())); 
            partyX = s[0]; 
            partyY = s[1]; 
        }

        tiles[partyY][partyX].setHasParty(true);

        if (ensurePath) {
            int attempts = 0; 
            while (!hasPathToAnyMarket() && attempts < 50) {
                attempts++; 
                int nx = rng.nextInt(w);
                int ny = rng.nextInt(h); 
                tiles[ny][nx].setType(TileType.COMMON); 
                tiles[ny][nx].setMarket(null); 
            } 
        }
    }

    private boolean hasPathToAnyMarket() {
        boolean[][] vis = new boolean[h][w]; 
        LinkedList<int[]> q = new LinkedList<int[]>();

        q.add(new int[]{partyX, partyY});
        vis[partyY][partyX] = true;
        int[][] dirs={{1, 0}, {-1, 0}, {0, 1}, {0, -1}};

        while(!q.isEmpty()){
            int[] p = q.removeFirst(); 
            int x = p[0];
            int y = p[1];
            if (tiles[y][x].getType() == TileType.MARKET){
                return true;
            }

            for (int[] d:dirs){
                int nx = x + d[0];
                int ny = y + d[1];
                if (nx >= 0 && ny >= 0 && nx < w && ny < h && !vis[ny][nx] && tiles[ny][nx].getType() != TileType.INACCESSIBLE){
                    vis[ny][nx]=true; 
                    q.add(new int[]{nx,ny});
                }
            }
        }

        return false;
    }

    public boolean moveParty(char dir) {
        int nx = partyX;
        int ny = partyY;
        
        switch(dir){
            case 'W':
                ny--; 
                break;
            case 'S':
                ny++;
                break;
            case 'A':
                nx--;
                break;
            case 'D':
                nx++;
                break;
        }

        if (nx < 0 || ny < 0 || nx >= w || ny >= h){
            return false;
        }

        if (tiles[ny][nx].getType() == TileType.INACCESSIBLE){
            return false;
        }

        tiles[partyY][partyX].setHasParty(false);
        partyX = nx; 
        partyY = ny;

        tiles[partyY][partyX].setHasParty(true);

        return true;
    }

    public Tile getTile(int x,int y){
        return tiles[y][x];
    }

    public boolean checkEncounter(){ 
        Tile t = getTile(partyX, partyY); 
        return t.getType() == TileType.COMMON && Math.random() < 0.40; 
    }

    public boolean isOnMarket(){ 
        return getTile(partyX, partyY).getType() == TileType.MARKET; 
    }
    
    public void enterMarket(java.util.Scanner scanner, GameUI ui){ 
        getTile(partyX, partyY).getMarket().openForParty(party, scanner, ui); 
    }

    public int getWidth(){
        return w;
    } 
    
    public int getHeight(){
        return h;
    }
}
