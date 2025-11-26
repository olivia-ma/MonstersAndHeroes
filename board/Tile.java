package board;

import markets.Market;
public class Tile { private TileType type = TileType.COMMON; private Market market = null; private boolean
hasParty = false; public TileType getType(){return type;} public void setType(TileType t){this.type=t;} public
Market getMarket(){return market;} public void setMarket(Market m){this.market=m;} public boolean
hasParty(){return hasParty;} public void setHasParty(boolean v){this.hasParty=v;} }
