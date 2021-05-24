package team;


import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**TODO
 *  RADI:
 *  -obavestavanje timova ko sve postoji
 *  -pull datoteke src/../a.txt timsko
 *  -----------------------------
 *  NE RADI:
 *  -verovatno sve to za src/../DIR/a.txt timsko
 *  -leader treba da osvezi globalnu kopiju
 *  -clanovi tima komituju od sada leadera
 *  -PULL HANDLER: daj mi samo file dir/asc.txt*/

public class LocalTeam {
    public static ConcurrentHashMap<String, Set<Integer>> teams = new ConcurrentHashMap<>();

}
