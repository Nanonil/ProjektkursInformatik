package projektkurs.world.builder;

import projektkurs.cutscene.CutScene;
import projektkurs.entity.EntityFerry;
import projektkurs.entity.EntityFerryhouse;
import projektkurs.entity.EntityFerryman;
import projektkurs.entity.EntityGrammophon;
import projektkurs.entity.EntityItem;
import projektkurs.entity.EntityRedNPC;
import projektkurs.entity.EntitySchranke;
import projektkurs.entity.EntityVilleCar;
import projektkurs.inventory.Inventory;
import projektkurs.item.ItemStack;
import projektkurs.lib.CutScenes;
import projektkurs.lib.Integers;
import projektkurs.lib.Items;
import projektkurs.lib.Raster;
import projektkurs.lib.Scripts;
import projektkurs.raster.AbstractRaster;
import projektkurs.raster.extra.ExtraInformation;
import projektkurs.raster.extra.ExtraInformationChest;
import projektkurs.raster.extra.ExtraInformationDoor;
import projektkurs.story.StoryManager;
import projektkurs.story.trigger.AreaTrigger;
import projektkurs.story.trigger.CombinedAndTrigger;
import projektkurs.story.trigger.InventoryHasItemStackTrigger;
import projektkurs.story.trigger.PosTrigger;
import projektkurs.story.trigger.Trigger;
import projektkurs.util.Direction;
import projektkurs.util.MapUtil;
import projektkurs.util.MathUtil;
import projektkurs.util.ReflectionUtil;
import projektkurs.world.Spielfeld;

/**
 * Spielfeld-Generierung.
 */
public final class MapBuilder {

    /**
     * Level 0 - Spielfeld 0.
     *
     * @param map
     *            Spielfeld
     */
    public static void generateAndPopulateLevel0Map0(Spielfeld map) {

        // RASEN!
        for (int y = 0; y < map.getMapSizeY(); y++) {
            for (int x = 0; x < map.getMapSizeX(); x++) {
                map.setRasterAt(x, y, Raster.rasen);
            }
        }

        // BAEUME!
        for (int i = 0; i < MathUtil.randomInt(25, 75); i++) {
            map.setRasterAt(MathUtil.nextInt(map.getMapSizeX()), MathUtil.nextInt(map.getMapSizeY()), Raster.baum);
        }

        // KISTEN!
        for (int i = 0; i < MathUtil.randomInt(10, 15); i++) {
            map.setRasterAt(MathUtil.nextInt(map.getMapSizeX()), MathUtil.nextInt(map.getMapSizeY()), Raster.chest);
        }
        map.setRasterAt(MathUtil.floorDiv(Integers.sightX, 2) - 1, MathUtil.floorDiv(Integers.sightY, 2) - 1, Raster.chest);

        // WAENDE!
        for (int x = 0; x < map.getMapSizeX(); x++) {
            map.setRasterAt(x, 0, Raster.wall);
            map.setRasterAt(x, map.getMapSizeY() - 1, Raster.wall);
        }
        for (int y = 0; y < map.getMapSizeY(); y++) {
            map.setRasterAt(0, y, Raster.wall);
            map.setRasterAt(map.getMapSizeX() - 1, y, Raster.wall);
        }

        // Animationen
        map.setRasterAt(3, 1, Raster.fire);

        // TUEREN!
        for (int y = 20; y < 25; y++) {
            for (int x = 18; x < 23; x++) {
                map.setRasterAt(x, y, Raster.rasen);
            }
        }
        map.setRasterAt(20, 18, Raster.baum);
        map.setRasterAt(21, 18, Raster.baum);
        // map.setRasterAt(22, 18, Raster.baum);
        map.setRasterAt(23, 18, Raster.baum);
        map.setRasterAt(24, 18, Raster.baum);
        map.setRasterAt(24, 19, Raster.baum);
        map.setRasterAt(24, 20, Raster.baum);
        map.setRasterAt(24, 21, Raster.baum);
        map.setRasterAt(24, 22, Raster.baum);
        map.setRasterAt(20, 19, Raster.baum);
        map.setRasterAt(20, 21, Raster.baum);
        map.setRasterAt(20, 22, Raster.baum);
        map.setRasterAt(21, 22, Raster.baum);
        map.setRasterAt(22, 22, Raster.baum);
        map.setRasterAt(23, 22, Raster.baum);
        map.setRasterAt(22, 20, Raster.finish);

        map.setRasterAt(20, 20, Raster.door);
        ExtraInformationDoor door = (ExtraInformationDoor) map.getExtraInformationAt(20, 20);
        door.setDirection(Direction.LEFT);
        door.setOpeningKey(1000);

        map.setRasterAt(22, 18, Raster.door);
        ExtraInformationDoor door2 = (ExtraInformationDoor) map.getExtraInformationAt(22, 18);
        door2.setDirection(Direction.UP);
        door2.setOpeningKey(1000);

        // KISTENINHALTE!
        Inventory inv;
        ExtraInformation extra;
        for (int y = 0; y < map.getMapSizeY(); y++) {
            for (int x = 0; x < map.getMapSizeX(); x++) {
                inv = new Inventory(Integers.CHEST_SIZE);
                inv.addItemStack(new ItemStack(Items.item42, 42));
                inv.addItemStack(new ItemStack(Items.nuke));
                inv.addItemStack(new ItemStack(Items.key));
                extra = map.getExtraInformationAt(x, y);
                if (extra instanceof ExtraInformationChest) {
                    ((ExtraInformationChest) extra).setInventory(inv);
                }
            }
        }

        // ENTITIES!
        for (int x = 0; x < 3; x++) {
            map.spawn(new EntityRedNPC(map, MathUtil.roundMul(Math.random(), 20) + 10, MathUtil.roundMul(Math.random(), 20) + 10));
        }

        // ITEMS
        map.spawn(new EntityItem(map, 5, 5, new ItemStack(Items.key, 1, 1000)));
        map.spawn(new EntityItem(map, 5, 6, new ItemStack(Items.item42, 42, 42)));
        map.spawn(new EntityItem(map, 5, 7, new ItemStack(Items.nuke, 1234)));
        map.spawn(new EntityItem(map, 5, 8, new ItemStack(Items.healthPotion, 1234, 100)));

        // STORYMAGER!
        map.getStoryManager().registerTrigger(new CombinedAndTrigger(new AreaTrigger(50, 50, 10, 10), new InventoryHasItemStackTrigger(new ItemStack(Items.nuke))), ReflectionUtil.getMethod(Scripts.class, "switchMap", Integer.TYPE), 1);

    }

    /**
     * Level 0 - Spielfeld 1.
     *
     * @param map
     *            Spielfeld
     */
    public static void generateAndPopulateLevel0Map1(Spielfeld map) {

        // DESTROYED-RASTER HINTERGRUND!
        for (int y = 0; y < map.getMapSizeY(); y++) {
            for (int x = 0; x < map.getMapSizeX(); x++) {
                map.setRasterAt(x, y, Raster.destroyedRaster);
            }
        }

        // WAeNDE!
        for (int x = 0; x < map.getMapSizeX(); x++) {
            map.setRasterAt(x, 0, Raster.wall);
            map.setRasterAt(x, map.getMapSizeY() - 1, Raster.wall);
        }
        for (int y = 0; y < map.getMapSizeY(); y++) {
            map.setRasterAt(0, y, Raster.wall);
            map.setRasterAt(map.getMapSizeX() - 1, y, Raster.wall);
        }

        // STORYMANAGER!
        map.getStoryManager().registerTrigger(new PosTrigger(18, 8), ReflectionUtil.getMethod(Scripts.class, "switchMap", Integer.TYPE), 0);
    }

    /**
     * Level 1 - Spielfeld 0.
     *
     * @param map
     *            Spielfeld
     */
    public static void generateAndPopulateLevel1Map0(Spielfeld map) {

        // Faehrman
        map.spawn(new EntityFerryman(map, 26, 26));

        // Grammophone

        map.spawn(new EntityGrammophon(map, 26, 28));

        // Ferry
        map.spawn(new EntityFerry(map, 10, 12));

        // // Fishership
        //
        // map.spawn(new EntityFisherboat(map, map.getMapSizeX() - 9, 40));

        // Auto
        map.spawn(new EntityVilleCar(map, 11, 25));

        // Schranke
        map.spawn(new EntitySchranke(map, 10, 20));

        // Boden

        for (int y = 20; y < map.getMapSizeY(); y++) {
            for (int x = 0; x < map.getMapSizeX(); x++) {
                map.setRasterAt(x, y, Raster.rasen_2);
            }
        }

        // Faehrhaus
        EntityFerryhouse fhouse = new EntityFerryhouse(map, 24, 24);
        map.spawn(fhouse);

        // Water

        for (int y = 0; y < 20; y++) {
            for (int x = 0; x < map.getMapSizeX(); x++) {
                map.setRasterAt(x, y, Raster.water);
            }
        }

        // Fisher

        // map.spawn(new EntityFisher(map, 60, 47));
        // Insel im Fluss
        int inttemp = 4;
        int inttemp2 = 0;
        for (int x = 0; x < 5; x++) {

            for (int y = 0; y < 5 - inttemp; y++) {
                map.setRasterAt(30 + x, 10 + y - inttemp2, Raster.floor_4);
            }
            inttemp = inttemp - 2;
            inttemp2++;
        }
        for (int y = 6; y < 15; y++) {
            for (int x = 35; x < 42; x++) {
                map.setRasterAt(x, y, Raster.floor_4);
            }
        }

        inttemp = 0;
        inttemp2 = 0;
        for (int x = 0; x < 5; x++) {

            for (int y = 0; y < 9 - inttemp; y++) {
                map.setRasterAt(42 + x, 6 + y + inttemp2, Raster.floor_4);
            }
            inttemp = inttemp + 2;
            inttemp2++;
        }
        for (int x = 36; x < 43; x++) {
            map.setRasterAt(x, 14, Raster.water);
        }

        for (int x = 38; x < 42; x++) {
            map.setRasterAt(x, 13, Raster.water);
        }

        for (int x = 34; x < 36; x++) {
            map.setRasterAt(x, 6, Raster.water);
        }

        for (int y = 8; y < 12; y++) {
            for (int x = 35; x < 43; x++) {
                map.setRasterAt(x, y, Raster.rasen_2);
            }
        }
        map.setRasterAt(35, 8, Raster.floor_4);

        map.setRasterAt(36, 8, Raster.floor_4);

        map.setRasterAt(41, 11, Raster.floor_4);

        map.setRasterAt(42, 11, Raster.floor_4);

        map.setRasterAt(42, 8, Raster.floor_4);

        map.setRasterAt(38, 11, Raster.floor_4);

        setTree(35, 10, Raster.tree_4nw, Raster.tree_4ne, Raster.tree_4se, Raster.tree_4sw, map);

        setTree(39, 9, Raster.tree_4nw, Raster.tree_4ne, Raster.tree_4se, Raster.tree_4sw, map);
        // Boden

        for (int y = 0; y < 8; y++) {

            for (int x = 0; x < 8 - y; x++) {
                map.setRasterAt(map.getMapSizeX() - x, y, Raster.rasen_2);
            }
        }

        // B�ume an der Stra�e

        for (int y = 21; y < map.getMapSizeY() - 2; y = y + 7) {
            setTree(7, y, Raster.tree_11nw, Raster.tree_11ne, Raster.tree_11se, Raster.tree_11sw, map);
            setTree(15, y + 3, Raster.tree_11nw, Raster.tree_11ne, Raster.tree_11se, Raster.tree_11sw, map);
        }

        // B�ume linke Seite
        for (int x = 2; x < 10; x = x + 2) {
            AbstractRaster[] temp = MapUtil.getRanTree();
            setTree(x, map.getMapSizeY() - 2, temp[0], temp[1], temp[2], temp[3], map);
        }

        for (int x = 2; x < 8; x = x + 2) {
            AbstractRaster[] temp = MapUtil.getRanTree();
            setTree(x, map.getMapSizeY() - 4, temp[0], temp[1], temp[2], temp[3], map);
        }

        for (int y = 22; y < map.getMapSizeY(); y = y + 2) {
            AbstractRaster[] temp = MapUtil.getRanTree();
            setTree(0, y, temp[0], temp[1], temp[2], temp[3], map);

        }

        for (int y = 48; y < map.getMapSizeY() - 4; y = y + 2) {
            AbstractRaster[] temp = MapUtil.getRanTree();
            setTree(2, y, temp[0], temp[1], temp[2], temp[3], map);
        }

        // Baumfeld in der Mitte

        for (int y = 0; y < 28; y = y + 2) {
            for (int x = 0; x < 10; x = x + 2) {
                AbstractRaster[] temp = MapUtil.getRanTree();
                setTree(40 + x, 28 + y, temp[0], temp[1], temp[2], temp[3], map);

            }

        }

        for (int y = 0; y < 22; y = y + 2) {
            for (int x = 0; x < 6; x = x + 2) {
                AbstractRaster[] temp = MapUtil.getRanTree();
                setTree(34 + x, 33 + y, temp[0], temp[1], temp[2], temp[3], map);

            }

        }

        for (int y = 0; y < 12; y = y + 2) {
            for (int x = 0; x < 4; x = x + 2) {
                AbstractRaster[] temp = MapUtil.getRanTree();
                setTree(30 + x, 38 + y, temp[0], temp[1], temp[2], temp[3], map);

            }

        }

        for (int y = 35; y < 53; y = y + 2) {
            AbstractRaster[] temp = MapUtil.getRanTree();
            setTree(50, y, temp[0], temp[1], temp[2], temp[3], map);
        }

        for (int y = 54; y < 58; y++) {
            for (int x = 44; x < 50; x++) {
                map.setRasterAt(x, y, Raster.rasen_2);
            }
        }

        // Baum oben rechts
        setTree(map.getMapSizeX() - 3, 1, Raster.tree_3nw, Raster.tree_3ne, Raster.tree_3se, Raster.tree_3sw, map);

        // Baumreihe ganz unten
        for (int x = 14; x < map.getMapSizeX(); x = x + 2) {
            AbstractRaster[] temp = MapUtil.getRanTree();
            setTree(x, map.getMapSizeY() - 2, temp[0], temp[1], temp[2], temp[3], map);
        }

        for (int x = 22; x < map.getMapSizeX(); x = x + 2) {
            AbstractRaster[] temp = MapUtil.getRanTree();
            setTree(x, map.getMapSizeY() - 4, temp[0], temp[1], temp[2], temp[3], map);
        }

        for (int x = 30; x < 52; x = x + 2) {
            AbstractRaster[] temp = MapUtil.getRanTree();
            setTree(x, map.getMapSizeY() - 6, temp[0], temp[1], temp[2], temp[3], map);
        }

        for (int x = 0; x < 8; x = x + 2) {
            AbstractRaster[] temp = MapUtil.getRanTree();
            setTree(34 + x, map.getMapSizeY() - 8, temp[0], temp[1], temp[2], temp[3], map);
        }

        for (int y = 56; y < map.getMapSizeY() - 4; y = y + 2) {
            for (int x = 66; x < map.getMapSizeX(); x = x + 2) {
                AbstractRaster[] temp = MapUtil.getRanTree();
                setTree(x, y, temp[0], temp[1], temp[2], temp[3], map);

            }
        }

        for (int y = map.getMapSizeY() - 2; y < map.getMapSizeY(); y++) {
            for (int x = 58; x < 64; x++) {
                map.setRasterAt(x, y, Raster.rasen_2);
            }
        }

        for (int y = map.getMapSizeY() - 4; y < map.getMapSizeY() - 2; y++) {
            for (int x = 56; x < 66; x++) {
                map.setRasterAt(x, y, Raster.rasen_2);
            }
        }

        // Strand am Boot

        for (int y = 36; y < 54; y++) {
            map.setRasterAt(61, y, Raster.floor_4);
        }

        for (int y = 39; y < 52; y++) {
            map.setRasterAt(60, y, Raster.floor_4);
        }

        for (int y = 41; y < 48; y++) {
            map.setRasterAt(59, y, Raster.floor_4);
        }

        inttemp = 0;
        for (int y = 49; y < 55; y++) {
            for (int x = 62; x < 63 + inttemp; x++) {
                map.setRasterAt(x, y, Raster.floor_4);
            }
            inttemp++;
        }

        map.setRasterAt(63, 50, Raster.water);

        map.setRasterAt(64, 51, Raster.water);

        map.setRasterAt(62, 49, Raster.water);

        // Water

        for (int y = 0; y < 20; y++) {

            for (int x = 0; x < 40 - y; x++) {
                map.setRasterAt(map.getMapSizeX() - x, 20 + y, Raster.water);
            }
        }

        for (int y = 30; y < 50; y++) {

            for (int x = map.getMapSizeX() - 28; x < map.getMapSizeX(); x++) {
                map.setRasterAt(x, y, Raster.water);
            }
        }

        for (int y = 0; y < 12; y++) {

            for (int x = 0; x < 28 - y; x++) {
                map.setRasterAt(map.getMapSizeX() - x, 50 + y, Raster.water);
            }
        }
        // Rasen unter dem Boot

        for (int x = 68; x < 72; x++) {
            map.setRasterAt(x, 55, Raster.rasen_2);
        }

        for (int x = 65; x < 76; x++) {
            map.setRasterAt(x, 56, Raster.rasen_2);
        }

        for (int y = 57; y < 62; y++) {

            for (int x = 65; x < map.getMapSizeX(); x++) {
                map.setRasterAt(x, y, Raster.rasen_2);
            }
        }

        for (int y = 62; y < 64; y++) {

            for (int x = 66; x < 74; x++) {
                map.setRasterAt(x, y, Raster.rasen_2);
            }
        }

        for (int y = 62; y < 66; y++) {

            for (int x = 66; x < 68; x++) {
                map.setRasterAt(x, y, Raster.rasen_2);
            }
        }
        // Sand Kurve unterm Boot
        for (int x = 65; x < 72; x++) {
            map.setRasterAt(x, 55, Raster.floor_4);
        }

        for (int x = 69; x < 76; x++) {
            map.setRasterAt(x, 56, Raster.floor_4);
        }

        for (int x = 74; x < map.getMapSizeX(); x++) {
            map.setRasterAt(x, 57, Raster.floor_4);
        }

        for (int x = 82; x < map.getMapSizeX(); x = x + 2) {
            AbstractRaster[] temp = MapUtil.getRanTree();
            setTree(x, 60, temp[0], temp[1], temp[2], temp[3], map);
        }

        map.setRasterAt(64, 52, Raster.water);

        // Baeume am Wasser

        setTree(42, 19, Raster.tree_5nw_water, Raster.tree_5ne_water, Raster.tree_5se, Raster.tree_5sw, map);

        setTree(53, 23, Raster.tree_5nw_water, Raster.tree_5ne_water, Raster.tree_5se, Raster.tree_5sw, map);

        setTree(51, 23, Raster.tree_5nw, Raster.tree_5ne, Raster.tree_5se, Raster.tree_5sw, map);

        setTree(51, 21, Raster.tree_5nw_water, Raster.tree_5ne_water, Raster.tree_5se_water, Raster.tree_5sw, map);

        setTree(48, 24, Raster.tree_6nw, Raster.tree_6ne, Raster.tree_6se, Raster.tree_6sw, map);

        setTree(49, 22, Raster.tree_5nw, Raster.tree_5ne, Raster.tree_5se, Raster.tree_5sw, map);

        setTree(47, 21, Raster.tree_6nw, Raster.tree_6ne, Raster.tree_6se, Raster.tree_6sw, map);

        setTree(47, 19, Raster.tree_5nw_water, Raster.tree_5ne_water, Raster.tree_5se, Raster.tree_5sw, map);

        map.setRasterAt(49, 20, Raster.water);

        map.setRasterAt(50, 20, Raster.water);

        // Haus

        for (int y = 25; y < 30; y++) {
            for (int x = 25; x < 28; x++) {
                map.setRasterAt(x, y, Raster.floor_12);
                map.setRasterAt(x, 24, Raster.ferryhouse_wall_n);
                map.setRasterAt(x, 30, Raster.ferryhouse_wall_s);
            }
            map.setRasterAt(24, y, Raster.ferryhouse_wall_w);
            map.setRasterAt(28, y, Raster.ferryhouse_wall_e);
        }
        map.setRasterAt(24, 24, Raster.ferryhouse_ecke_nw);
        map.setRasterAt(28, 24, Raster.ferryhouse_ecke_ne);
        map.setRasterAt(24, 30, Raster.ferryhouse_ecke_sw);
        map.setRasterAt(28, 30, Raster.ferryhouse_ecke_se);

        map.setRasterAt(26, 27, Raster.chair_2);

        // map.setRasterAt(27, 17, Raster.ferryhouse_door);

        for (int i = 0; i < 8; i++) {
            setStreet(10, 20 + i * 8, map);
        }

        // Hafenbegrenzung

        for (int x = 0; x < 30; x++) {

            map.setRasterAt(x, 20, Raster.cobbles_2);
        }

        for (int x = 0; x < 23; x++) {

            map.setRasterAt(4 + x, 19, Raster.cobbles_2);
        }

        for (int x = 0; x < 8; x++) {

            map.setRasterAt(15 + x, 18, Raster.cobbles_2);
        }

        // Trigger

        StoryManager st = map.getStoryManager();

        // CombinedAndTrigger trigger = new CombinedAndTrigger(new PosTrigger(30,
        // 30), new AreaTrigger(30, 30, 10, 10),
        // new InventoryHasItemStackTrigger(new ItemStack(Items.earrings)));

        // Faehrhaus spawnen/despawnen
        AreaTrigger area = new AreaTrigger(19, 20, 15, 16);
        st.registerTrigger(area, Scripts.REMOVE_ENTITY, fhouse, map);
        // mapwechsel
        AreaTrigger mapswitch = new AreaTrigger(58, 69, 6, 1);
        st.registerTrigger(mapswitch, ReflectionUtil.getMethod(Scripts.class, "switchMap", Integer.TYPE), 1);

        // Cutscenestart
        AreaTrigger dia1 = new AreaTrigger(10, 22, 6, 1);
        st.registerTrigger(dia1, ReflectionUtil.getMethod(Scripts.class, "cutscenestart", CutScene.class, Spielfeld.class, Trigger.class), CutScenes.one, map, dia1);

    }

    /**
     * Level 1 - Spielfeld 1.
     *
     * @param map
     *            Spielfeld
     */
    public static void generateAndPopulateLevel1Map1(Spielfeld map) {
        for (int y = 0; y < map.getMapSizeY(); y++) {
            for (int x = 0; x < map.getMapSizeX(); x++) {
                map.setRasterAt(x, y, Raster.rasen_2);
            }
        }

    }

    private static void setStreet(int x, int y, Spielfeld map) {
        map.setRasterAt(x + 1, y, Raster.street_l_b_1_senk);
        map.setRasterAt(x + 1, y + 1, Raster.street_l_b_2_senk);
        map.setRasterAt(x + 1, y + 2, Raster.street_l_b_3_senk);
        map.setRasterAt(x + 1, y + 3, Raster.street_l_b_4_senk);
        map.setRasterAt(x + 1, y + 4, Raster.street_l_b_5_senk);
        map.setRasterAt(x + 1, y + 5, Raster.street_l_b_6_senk);
        map.setRasterAt(x + 1, y + 6, Raster.street_l_b_7_senk);
        map.setRasterAt(x + 1, y + 7, Raster.street_l_b_8_senk);
        map.setRasterAt(x + 2, y, Raster.street_l_t_1_senk);
        map.setRasterAt(x + 2, y + 1, Raster.street_l_t_2_senk);
        map.setRasterAt(x + 2, y + 2, Raster.street_l_t_3_senk);
        map.setRasterAt(x + 2, y + 3, Raster.street_l_t_4_senk);
        map.setRasterAt(x + 2, y + 4, Raster.street_l_t_5_senk);
        map.setRasterAt(x + 2, y + 5, Raster.street_l_t_6_senk);
        map.setRasterAt(x + 2, y + 6, Raster.street_l_t_7_senk);
        map.setRasterAt(x + 2, y + 7, Raster.street_l_t_8_senk);
        for (int i = 0; i < 8; i++) {
            map.setRasterAt(x, y + i, Raster.street_asphalt);
            map.setRasterAt(x + 3, y + i, Raster.street_asphalt);
        }

    }

    private static void setTree(int x, int y, AbstractRaster one, AbstractRaster two, AbstractRaster three, AbstractRaster four, Spielfeld map) {

        map.setRasterAt(x, y, one);
        map.setRasterAt(x + 1, y, two);
        map.setRasterAt(x + 1, y + 1, three);
        map.setRasterAt(x, y + 1, four);
    }

    /**
     * Nicht instanziierbar.
     */
    private MapBuilder() {
    }
}
