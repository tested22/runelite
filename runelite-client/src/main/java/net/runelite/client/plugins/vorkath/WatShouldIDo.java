package net.runelite.client.plugins.vorkath;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
@Slf4j
public class WatShouldIDo {

    @Inject
    private Client client;

    public void attackVorkath() {
        System.out.println("I Should attack vork");
    }

    public void togglePrayer() {
        System.out.println("I Should toggle quick prayers");
    }


    public void eatFood() {
        System.out.println("I Should eat food");
    }

    public void homeTele() {
        System.out.println("I Should tele home");
    }

    public void crumbleSpawn() {
        System.out.println("I Should crumble the spawn");
    }

    public void drinkPot() {
        System.out.println("I Should drink prayer pot");
    }

    public void curePoison() {
        System.out.println("I Should cure poison");
    }

    public void clickMarkers() {
        System.out.println("I Should click tile marker");
    }
}