package net.runelite.client.plugins.vorkath;

import net.runelite.api.Client;
import net.runelite.api.coords.WorldPoint;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;

@Slf4j
public class SafeTiles {

    @Inject
    Client client;

    Set<WorldPoint> poisonSet = new HashSet<>();
    Set<WorldPoint> validTilesSet = new HashSet<>();
    Set<WorldPoint> vorkTilesSet = new HashSet<>();
    Set<WorldPoint> attackTilesSet = new HashSet<>();

    ArrayList<WorldPoint> WPList = new ArrayList<>();

    WorldPoint vorkLocation = new WorldPoint(0,0,0);

    public void atVorkath(int x, int y){
        getVorkTiles(x, y);
        getValidTiles(x, y);
        getAttackTiles(x,y);
        System.out.println("at vork");
        System.out.println("Valid Tiles: " + validTilesSet.toString());
        System.out.println("Vorkath Tiles: " + vorkTilesSet.toString());
        System.out.println("Attack Tiles: " + attackTilesSet.toString());
    }

    public void findSafeTile() {
        System.out.println("attempting to find safe tile");
        WorldPoint playerLocation = client.getLocalPlayer().getWorldLocation();
        int px = playerLocation.getX();
        int py = playerLocation.getY();

        for (int dx = -1; dx <= 1; dx++)
        {
            if (dx == 0)
            {
                continue;
            }
            ArrayList<WorldPoint> twpList = new ArrayList<>();
            for (int i = 1; i <= 3; i++)
            {
                twpList.add(new WorldPoint(px+i*dx, py, 0));
            }
            if (validPath(twpList) && !poisonSet.contains(twpList.get(0)) && !poisonSet.contains(twpList.get(1))){
                WorldPoint wp = new WorldPoint(px+3*dx, py, 0);
                System.out.println("1 " + dx + " " + wp.toString());
                WPList.clear();
                WPList.add(wp);
                return;
            }
        }
        for (int dy = -1; dy <= 1; dy++)
        {
            if (dy == 0)
            {
                continue;
            }
            ArrayList<WorldPoint> twpList = new ArrayList<>();
            for (int i = 1; i <= 3; i++)
            {
                twpList.add(new WorldPoint(px, py+i*dy, 0));
            }
            if (validPath(twpList) && !poisonSet.contains(twpList.get(0)) && !poisonSet.contains(twpList.get(1))){
                WorldPoint wp = new WorldPoint(px, py+3*dy, 0);
                System.out.println("2 "+ dy + " " + wp.toString());
                WPList.clear();
                WPList.add(wp);
                return;
            }
        }
        //if it cant go horizontal or vertical, search diagonal
        for (int dx = -1; dx <= 1; dx++)
        {
            for (int dy = -1; dy <= 1; dy++)
            {
                if (dx == 0 || dy == 0)
                {
                    continue;
                }
                ArrayList<WorldPoint> twpList = new ArrayList<>();
                for (int i = 1; i <= 3; i++)
                {
                    twpList.add(new WorldPoint(px+i*dx, py+i*dy, 0));
                }
                if (validPath(twpList) && !poisonSet.contains(twpList.get(0))  && !poisonSet.contains(twpList.get(1))){
                    WorldPoint wp = new WorldPoint(px+3*dx, py+3*dy, 0);
                    System.out.println("3 "+ dx + " " + dy + " " + wp.toString());
                    WPList.clear();
                    WPList.add(wp);
                    return;
                }
            }
        }
        for (int dx = -1; dx <= 1; dx++)
        {
            if (dx == 0)
            {
                continue;
            }
            ArrayList<WorldPoint> twpList = new ArrayList<>();
            for (int i = 1; i <= 3; i++)
            {
                twpList.add(new WorldPoint(px+i*dx, py, 0));
            }
            if (validPath(twpList) && !poisonSet.contains(twpList.get(0))){
                WorldPoint wp = new WorldPoint(px+3*dx, py, 0);
                System.out.println("4 " + dx + " " + wp.toString());
                WPList.clear();
                WPList.add(wp);
                return;
            }
        }
        for (int dy = -1; dy <= 1; dy++)
        {
            if (dy == 0)
            {
                continue;
            }
            ArrayList<WorldPoint> twpList = new ArrayList<>();
            for (int i = 1; i <= 3; i++)
            {
                twpList.add(new WorldPoint(px, py+i*dy, 0));
            }
            if (validPath(twpList) && !poisonSet.contains(twpList.get(0))){
                WorldPoint wp = new WorldPoint(px, py+3*dy, 0);
                System.out.println("5 " + dy + " " + wp.toString());
                WPList.clear();
                WPList.add(wp);
                return;
            }
        }
        //if it cant go horizontal or vertical, search diagonal
        for (int dx = -1; dx <= 1; dx++)
        {
            for (int dy = -1; dy <= 1; dy++)
            {
                if (dx == 0 || dy == 0)
                {
                    continue;
                }
                ArrayList<WorldPoint> twpList = new ArrayList<>();
                for (int i = 1; i <= 3; i++)
                {
                    twpList.add(new WorldPoint(px+i*dx, py+i*dy, 0));
                }
                if (validPath(twpList) && !poisonSet.contains(twpList.get(0))){
                    WorldPoint wp = new WorldPoint(px+3*dx, py+3*dy, 0);
                    System.out.println("6 " + dx + " " + dy + " " + wp.toString());
                    WPList.clear();
                    WPList.add(wp);
                    return;
                }
            }
        }

        for (int dx = -1; dx <= 1; dx++)
        {
            if (dx == 0)
            {
                continue;
            }
            ArrayList<WorldPoint> twpList = new ArrayList<>();
            for (int i = 1; i <= 2; i++)
            {
                twpList.add(new WorldPoint(px+i*dx, py, 0));
            }
            if (validPath(twpList) && !poisonSet.contains(twpList.get(0)) && !poisonSet.contains(twpList.get(1))){
                WorldPoint wp = new WorldPoint(px+3*dx, py, 0);
                System.out.println("1 " + dx + " " + wp.toString());
                WPList.clear();
                WPList.add(wp);
                return;
            }
        }
        for (int dy = -1; dy <= 1; dy++)
        {
            if (dy == 0)
            {
                continue;
            }
            ArrayList<WorldPoint> twpList = new ArrayList<>();
            for (int i = 1; i <= 2; i++)
            {
                twpList.add(new WorldPoint(px, py+i*dy, 0));
            }
            if (validPath(twpList) && !poisonSet.contains(twpList.get(0)) && !poisonSet.contains(twpList.get(1))){
                WorldPoint wp = new WorldPoint(px, py+3*dy, 0);
                System.out.println("2 "+ dy + " " + wp.toString());
                WPList.clear();
                WPList.add(wp);
                return;
            }
        }
        //if it cant go horizontal or vertical, search diagonal
        for (int dx = -1; dx <= 1; dx++)
        {
            for (int dy = -1; dy <= 1; dy++)
            {
                if (dx == 0 || dy == 0)
                {
                    continue;
                }
                ArrayList<WorldPoint> twpList = new ArrayList<>();
                for (int i = 1; i <= 2; i++)
                {
                    twpList.add(new WorldPoint(px+i*dx, py+i*dy, 0));
                }
                if (validPath(twpList) && !poisonSet.contains(twpList.get(0))  && !poisonSet.contains(twpList.get(1))){
                    WorldPoint wp = new WorldPoint(px+3*dx, py+3*dy, 0);
                    System.out.println("3 "+ dx + " " + dy + " " + wp.toString());
                    WPList.clear();
                    WPList.add(wp);
                    return;
                }
            }
        }
        for (int dx = -1; dx <= 1; dx++)
        {
            if (dx == 0)
            {
                continue;
            }
            ArrayList<WorldPoint> twpList = new ArrayList<>();
            for (int i = 1; i <= 2; i++)
            {
                twpList.add(new WorldPoint(px+i*dx, py, 0));
            }
            if (validPath(twpList) && !poisonSet.contains(twpList.get(0))){
                WorldPoint wp = new WorldPoint(px+3*dx, py, 0);
                System.out.println("4 " + dx + " " + wp.toString());
                WPList.clear();
                WPList.add(wp);
                return;
            }
        }
        for (int dy = -1; dy <= 1; dy++)
        {
            if (dy == 0)
            {
                continue;
            }
            ArrayList<WorldPoint> twpList = new ArrayList<>();
            for (int i = 1; i <= 2; i++)
            {
                twpList.add(new WorldPoint(px, py+i*dy, 0));
            }
            if (validPath(twpList) && !poisonSet.contains(twpList.get(0))){
                WorldPoint wp = new WorldPoint(px, py+3*dy, 0);
                System.out.println("5 " + dy + " " + wp.toString());
                WPList.clear();
                WPList.add(wp);
                return;
            }
        }
        //if it cant go horizontal or vertical, search diagonal
        for (int dx = -1; dx <= 1; dx++)
        {
            for (int dy = -1; dy <= 1; dy++)
            {
                if (dx == 0 || dy == 0)
                {
                    continue;
                }
                ArrayList<WorldPoint> twpList = new ArrayList<>();
                for (int i = 1; i <= 2; i++)
                {
                    twpList.add(new WorldPoint(px+i*dx, py+i*dy, 0));
                }
                if (validPath(twpList) && !poisonSet.contains(twpList.get(0))){
                    WorldPoint wp = new WorldPoint(px+3*dx, py+3*dy, 0);
                    System.out.println("6 " + dx + " " + dy + " " + wp.toString());
                    WPList.clear();
                    WPList.add(wp);
                    return;
                }
            }
        }

        for (int dx = -1; dx <= 1; dx++)
        {
            if (dx == 0)
            {
                continue;
            }
            ArrayList<WorldPoint> twpList = new ArrayList<>();
            for (int i = 1; i <= 1; i++)
            {
                twpList.add(new WorldPoint(px+i*dx, py, 0));
            }
            if (validPath(twpList) && !poisonSet.contains(twpList.get(0)) && !poisonSet.contains(twpList.get(1))){
                WorldPoint wp = new WorldPoint(px+3*dx, py, 0);
                System.out.println("1 " + dx + " " + wp.toString());
                WPList.clear();
                WPList.add(wp);
                return;
            }
        }
        for (int dy = -1; dy <= 1; dy++)
        {
            if (dy == 0)
            {
                continue;
            }
            ArrayList<WorldPoint> twpList = new ArrayList<>();
            for (int i = 1; i <= 1; i++)
            {
                twpList.add(new WorldPoint(px, py+i*dy, 0));
            }
            if (validPath(twpList) && !poisonSet.contains(twpList.get(0)) && !poisonSet.contains(twpList.get(1))){
                WorldPoint wp = new WorldPoint(px, py+3*dy, 0);
                System.out.println("2 "+ dy + " " + wp.toString());
                WPList.clear();
                WPList.add(wp);
                return;
            }
        }
        //if it cant go horizontal or vertical, search diagonal
        for (int dx = -1; dx <= 1; dx++)
        {
            for (int dy = -1; dy <= 1; dy++)
            {
                if (dx == 0 || dy == 0)
                {
                    continue;
                }
                ArrayList<WorldPoint> twpList = new ArrayList<>();
                for (int i = 1; i <= 1; i++)
                {
                    twpList.add(new WorldPoint(px+i*dx, py+i*dy, 0));
                }
                if (validPath(twpList) && !poisonSet.contains(twpList.get(0))  && !poisonSet.contains(twpList.get(1))){
                    WorldPoint wp = new WorldPoint(px+3*dx, py+3*dy, 0);
                    System.out.println("3 "+ dx + " " + dy + " " + wp.toString());
                    WPList.clear();
                    WPList.add(wp);
                    return;
                }
            }
        }
        for (int dx = -1; dx <= 1; dx++)
        {
            if (dx == 0)
            {
                continue;
            }
            ArrayList<WorldPoint> twpList = new ArrayList<>();
            for (int i = 1; i <= 1; i++)
            {
                twpList.add(new WorldPoint(px+i*dx, py, 0));
            }
            if (validPath(twpList) && !poisonSet.contains(twpList.get(0))){
                WorldPoint wp = new WorldPoint(px+3*dx, py, 0);
                System.out.println("4 " + dx + " " + wp.toString());
                WPList.clear();
                WPList.add(wp);
                return;
            }
        }
        for (int dy = -1; dy <= 1; dy++)
        {
            if (dy == 0)
            {
                continue;
            }
            ArrayList<WorldPoint> twpList = new ArrayList<>();
            for (int i = 1; i <= 1; i++)
            {
                twpList.add(new WorldPoint(px, py+i*dy, 0));
            }
            if (validPath(twpList) && !poisonSet.contains(twpList.get(0))){
                WorldPoint wp = new WorldPoint(px, py+3*dy, 0);
                System.out.println("5 " + dy + " " + wp.toString());
                WPList.clear();
                WPList.add(wp);
                return;
            }
        }
        //if it cant go horizontal or vertical, search diagonal
        for (int dx = -1; dx <= 1; dx++)
        {
            for (int dy = -1; dy <= 1; dy++)
            {
                if (dx == 0 || dy == 0)
                {
                    continue;
                }
                ArrayList<WorldPoint> twpList = new ArrayList<>();
                for (int i = 1; i <= 1; i++)
                {
                    twpList.add(new WorldPoint(px+i*dx, py+i*dy, 0));
                }
                if (validPath(twpList) && !poisonSet.contains(twpList.get(0))){
                    WorldPoint wp = new WorldPoint(px+3*dx, py+3*dy, 0);
                    System.out.println("6 " + dx + " " + dy + " " + wp.toString());
                    WPList.clear();
                    WPList.add(wp);
                    return;
                }
            }
        }

        System.out.println("none found");
        WorldPoint twp = new WorldPoint(px-3, py, 0);
        if (validTilesSet.contains(twp)){
            System.out.println("none found, moving marker left");
            WPList.clear();
            WPList.add(twp);
            return;
        }
        //else
        System.out.println("none found, moving marker right");
        WPList.clear();
        WPList.add(new WorldPoint(px+3, py, 0));

    }

    public boolean validPath(ArrayList<WorldPoint> twpList){
        for (WorldPoint twp: twpList){
            if (!validTile(twp)){
                return false;
            }
        }
        return true;
    }

    public boolean validTile(WorldPoint twp){
        if ( (validTilesSet.contains(twp) && !vorkTilesSet.contains(twp))){
            return true;
        }
        return false;
    }

    public void getValidTiles(int px, int py){
        validTilesSet.clear();
        for (int dx = px-10; dx <= px+10; dx++)
        {
            for (int dy = py+1; dy <= py+30; dy++){
                validTilesSet.add(new WorldPoint(dx, dy, 0));
            }
        }
    }

    public void getVorkTiles(int px, int py){
        vorkTilesSet.clear();
        for (int dx = px-5; dx <= px+5; dx++)
        {
            for (int dy = py+6; dy <= py+16; dy++){
                vorkTilesSet.add(new WorldPoint(dx, dy, 0));
            }
        }
    }

    public void getAttackTiles(int px, int py){
        attackTilesSet.clear();
        for (int dx = px-8; dx <= px+8; dx++)
        {
            for (int dy = py+3; dy <= py+19; dy++){
                attackTilesSet.add(new WorldPoint(dx, dy, 0));
            }
        }
    }

    public void safeAOESpot(){
        System.out.println("searching for bomb safespot");
        WorldPoint playerLocation = client.getLocalPlayer().getWorldLocation();
        int px = playerLocation.getX();
        int py = playerLocation.getY();

        for (int dx = -1; dx <= 1; dx++)
        {
            for (int dy = -1; dy <= 1; dy++)
            {
                //System.out.println("searching tile for bomb safespot dx: " + dx + " dy: +" + dy);
                if (dx == 0 && dy == 0)
                {
                    continue;
                }
                WorldPoint twp = new WorldPoint(px+2*dx,py+2*dy, 0);
                if (!vorkTilesSet.contains(twp) && attackTilesSet.contains(twp)){
                    System.out.println("found");
                    WPList.clear();
                    WPList.add(twp);
                    return;
                }
            }
        }

    //if all tiles are vorkTiles, run left two squares, or right two squares
        WorldPoint twp = new WorldPoint(px-2, py, 0);
        if (validTilesSet.contains(twp)){
            System.out.println("none found, moving left");
            WPList.clear();
            WPList.add(twp);
            return;
        }
        //else
        System.out.println("none found, moving right");
        WPList.clear();
        WPList.add(new WorldPoint(px+2, py, 0));
    }

    public void runRight(){
        WorldPoint playerLocation = client.getLocalPlayer().getWorldLocation();
        int px = playerLocation.getX();
        int py = playerLocation.getY();
        WorldPoint wp = new WorldPoint(px+4, py, 0);
        WPList.clear();
        WPList.add(wp);
    }
}
