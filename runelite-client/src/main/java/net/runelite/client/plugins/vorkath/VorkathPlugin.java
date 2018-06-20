package net.runelite.client.plugins.vorkath;
import com.google.common.eventbus.Subscribe;
import net.runelite.api.*;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.*;
import net.runelite.api.kit.KitType;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.api.widgets.WidgetItem;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;
import net.runelite.client.util.QueryRunner;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collections;

import static net.runelite.api.Prayer.EAGLE_EYE;

@PluginDescriptor(
        name = "Vorkath"
)
public class VorkathPlugin extends Plugin
{
    @Inject
    private Client client;

    @Inject
    private QueryRunner queryRunner;

    @Inject
    private VorkathOverlay vorkathOverlay;
    @Inject
    private TileOverlay tileOverlay;
    @Inject
    private ConfigManager configManager;
    @Inject
    private OverlayManager overlayManager;
    @Inject
    SafeTiles safeTiles = new SafeTiles();

    WatShouldIDo watShouldIDo = new WatShouldIDo();

    private VorkathAttack attack;

    int vorkathAttackCounter = 6;
    String vorkathNextSpecial = "Unknown";

    boolean vorkathIsAlive = false;
    boolean poisonActive = false;
    boolean zombieSpawned = false;
    boolean prayerCheck = false;
    boolean attackCheck = false;
    boolean specsUsed = false;
    boolean venomed = false;

    Actor actor;

    @Override
    protected void startUp() {
        overlayManager.add(vorkathOverlay);
        overlayManager.add(tileOverlay);
    }

    @Override
    protected void shutDown() throws Exception {
        overlayManager.removeIf(VorkathOverlay.class::isInstance);
        overlayManager.removeIf(TileOverlay.class::isInstance);
        //executor.shutdown();
    }

    @Subscribe
    public void onAimationChanged(AnimationChanged event) {
        if (client.getGameState() != GameState.LOGGED_IN || !client.isInInstancedRegion())
        {
            vorkathIsAlive = false;
            vorkathAttackCounter = 6;
            vorkathNextSpecial = "Unknown";
            return;
        }

        actor = event.getActor();

        if ("Vorkath".equals(actor.getName())) {
            vorkathIsAlive = true;
            safeTiles.vorkLocation = actor.getWorldLocation();

            //if player is standing on marked tile
            if (!safeTiles.WPList.isEmpty()
                    && safeTiles.WPList.get(0).equals(client.getLocalPlayer().getWorldLocation())){
                attackCheck = true;
                System.out.println("on safespot, clearing");
                safeTiles.WPList.clear();
            }

            if ((actor.getAnimation() == VorkathAttack.REGULAR.getAnimation()
                    || actor.getAnimation() == VorkathAttack.MELEE.getAnimation()
                    || actor.getAnimation() == VorkathAttack.TOSS.getAnimation()) && vorkathAttackCounter > 0) {
                attackCheck = true;
                if (actor.getAnimation() == VorkathAttack.TOSS.getAnimation()) {
                    attackCheck = false;
                    System.out.println("bomb");
                    safeTiles.safeAOESpot();
                    System.out.println("finished? adding safe aoe spot");
                }
                attack = VorkathAttack.REGULAR;
                vorkathAttackCounter--;
                System.out.println("regular animation, decrementing attack counter: " + vorkathAttackCounter);
            }
            else if (actor.getAnimation() == VorkathAttack.TOSS.getAnimation() && vorkathAttackCounter == 0) {
                attackCheck = false;
                prayerCheck = false;
                zombieSpawned = true;
                watShouldIDo.togglePrayer();
                attack = VorkathAttack.TOSS;
                vorkathAttackCounter = 6;
                vorkathNextSpecial = "Poison";
                System.out.println("ice phase, reset attack counter: " + vorkathAttackCounter);
            }
            else if (actor.getAnimation() == VorkathAttack.POISON.getAnimation()) {
                if(!poisonActive){
                    poisonActive = true;
                    safeTiles.runRight();
                    prayerCheck = false;
                    attackCheck = false;
                }
                attack = VorkathAttack.POISON;
                vorkathAttackCounter = 6;
                vorkathNextSpecial = "Ice";
                System.out.println("poison phase, reset attack counter: " + vorkathAttackCounter);
                System.out.println("poisonActive: " + poisonActive);
            }
            else if (actor.getAnimation() == VorkathAttack.DEATH.getAnimation()) {
                poisonActive = false;
                zombieSpawned = false;
                prayerCheck = false;
                attackCheck = false;
                specsUsed = false;
                safeTiles.WPList.clear();
                attack = VorkathAttack.DEATH;
                vorkathAttackCounter = 6;
                vorkathNextSpecial = "Unknown";
            }
            else {
                attack = null;
            }
        }
    }

    @Subscribe
    public void onGameObjectSpawned(GameObjectSpawned event) {
        int id = event.getGameObject().getId();
        if (id == 32000) {
            WorldPoint tempWP = event.getGameObject().getWorldLocation();
            safeTiles.poisonSet.add(tempWP);
        }
    }

    @Subscribe
    public void onGameObjectDespawned(GameObjectDespawned event) {
        int id = event.getGameObject().getId();
        if (id == 32000) {
            if (poisonActive){
                System.out.println("poison despawned, poison phase off");
                poisonActive = false;
                safeTiles.poisonSet.clear();
                safeTiles.WPList.clear();
                prayerCheck = true;
                attackCheck = true;
            }
        }
    }

    @Subscribe
    public void onNpcDespawned(NpcDespawned event) {
        if (event.getActor().getCombatLevel() == 64 && zombieSpawned){
            zombieSpawned = false;
        }
    }

    @Subscribe
    public void onHitSplatApplied(HitsplatApplied event){
        Actor actor = event.getActor();
        if (actor.getCombatLevel() == 64){
            zombieSpawned = false;
            prayerCheck = true;
            attackCheck = true;
        }
        else if (event.getHitsplat().getHitsplatType().equals(Hitsplat.HitsplatType.VENOM) && event.getActor() == client.getLocalPlayer()){
            venomed = true;
        }
    }

    @Subscribe
    public void onGameTick(GameTick tick)
    {
        if (venomed){
            watShouldIDo.curePoison();
        }

        if(!vorkathIsAlive){
            return;
        }

        //ONLY TELLS ME WHAT TO DO AFTER I SPEC WITH DWH/BGS AND I HAVE BLOWPIPE AND VOID MELEE HELM EQUIPPED
        if (client.getVar(VarPlayer.SPECIAL_ATTACK_PERCENT) < 500
                && client.getLocalPlayer().getPlayerComposition().getEquipmentId(KitType.WEAPON) == 12926
        && 11664 == (client.getItemContainer(InventoryID.EQUIPMENT).getItems()[0].getId()) )
        {
            if(!specsUsed) {
                specsUsed = true;
                watShouldIDo.togglePrayer();
            }
        }
        else if (!specsUsed){
            return;
        }

        //TELLS ME TO run away from tick fire during poison phase
        if (poisonActive){
            System.out.println("poison active, attempting to find safe tile");
            safeTiles.findSafeTile();
            System.out.println("player location: " + client.getLocalPlayer().getWorldLocation().toString());
        }

        eat();

        if (client.getBoostedSkillLevel(Skill.PRAYER)  <= 10
                && !poisonActive && !zombieSpawned){
            watShouldIDo.drinkPot();
        }

        if (prayerCheck && !client.isPrayerActive(EAGLE_EYE)){
            watShouldIDo.togglePrayer();
        }
        else if (!prayerCheck && client.isPrayerActive(EAGLE_EYE)){
            watShouldIDo.togglePrayer();
        }

        if (zombieSpawned){
            watShouldIDo.crumbleSpawn();
        }

        if (attackCheck && client.getLocalPlayer().getInteracting() == null){
            watShouldIDo.attackVorkath();
        }
    }

        @Subscribe
        public void onChatMessage(ChatMessage event) {
            if (event.getType() == ChatMessageType.SERVER && event.getMessage().contains("climb over the ice")) {
                poisonActive = false;
                zombieSpawned = false;
                prayerCheck = true;
                attackCheck = false;
                specsUsed = false;
                WorldPoint playerLocation = client.getLocalPlayer().getWorldLocation();
                int px = playerLocation.getX();
                int py = playerLocation.getY();
                safeTiles.atVorkath(px, py);
                watShouldIDo.clickMarkers();
            }
            else if (event.getType() == ChatMessageType.SERVER && event.getMessage().contains("disabled")) {
                watShouldIDo.togglePrayer();
            }

            else if (event.getType() == ChatMessageType.SERVER && event.getMessage().contains("cures")) {
                venomed = false;
            }


    }


    public void eat(){
        if (client.getBoostedSkillLevel(Skill.HITPOINTS)  > 0
                && client.getBoostedSkillLevel(Skill.HITPOINTS)  <= 58
                && !poisonActive && !zombieSpawned){
            if (client.getWidget(WidgetInfo.INVENTORY) != null){
                for (WidgetItem item: Collections.unmodifiableCollection(client.getWidget(WidgetInfo.INVENTORY).getWidgetItems())){
                    if (item.getId() == ItemID.MANTA_RAY || item.getId() == ItemID.COOKED_KARAMBWAN){
                        watShouldIDo.eatFood();
                        return;
                    }
                }
            }
            watShouldIDo.homeTele();
        }
    }

    VorkathAttack getAttack() {
        return attack;
    }

    ArrayList<WorldPoint> getPoints(){
        return safeTiles.WPList;
    }

}