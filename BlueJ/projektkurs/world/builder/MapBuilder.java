package projektkurs.world.builder;

import projektkurs.entity.Entity;
import projektkurs.entity.EntityFerry;
import projektkurs.entity.EntityItem;
import projektkurs.entity.EntityRedNPC;
import projektkurs.inventory.Inventory;
import projektkurs.item.ItemStack;
import projektkurs.lib.Integers;
import projektkurs.lib.Items;
import projektkurs.lib.Raster;
import projektkurs.lib.Scripts;
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

        // BAeUME!
        for (int i = 0; i < MathUtil.randomInt(25, 75); i++) {
            map.setRasterAt(MathUtil.nextInt(map.getMapSizeX()), MathUtil.nextInt(map.getMapSizeY()), Raster.baum);
        }

        // KISTEN!
        for (int i = 0; i < MathUtil.randomInt(10, 15); i++) {
            map.setRasterAt(MathUtil.nextInt(map.getMapSizeX()), MathUtil.nextInt(map.getMapSizeY()), Raster.chest);
        }
        map.setRasterAt(MathUtil.floorDiv(Integers.sightX, 2) - 1, MathUtil.floorDiv(Integers.sightY, 2) - 1, Raster.chest);

        // WAeNDE!
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

        // TUeREN!
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
        for (int y = 0; y < 20; y++) {
            for (int x = 0; x < map.getMapSizeX(); x++) {
                map.setRasterAt(x, y, Raster.water);
            }
        }

        // EntityFerry ferry = new EntityFerry(map, 10, 12);

        for (int y = 21; y < map.getMapSizeY(); y++) {
            for (int x = 0; x < map.getMapSizeX(); x++) {
                map.setRasterAt(x, y, Raster.rasen_2);
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

        StoryManager st = map.getStoryManager();
        AreaTrigger area = new AreaTrigger(40, 0, 5, 10);
        PosTrigger pt = new PosTrigger(20, 20);
        CombinedAndTrigger trigger = new CombinedAndTrigger(new PosTrigger(30, 30), new AreaTrigger(30, 30, 10, 10), new InventoryHasItemStackTrigger(new ItemStack(Items.earrings)));
        st.registerTrigger(pt, ReflectionUtil.getMethod(Scripts.class, "test", Entity.class, Spielfeld.class, Trigger.class), new EntityFerry(map, 20, 20), map, trigger);
        st.registerTrigger(area, ReflectionUtil.getMethod(Scripts.class, "switchMap", Integer.TYPE), 1);

    }

    /**
     * Nicht instanziierbar.
     */
    private MapBuilder() {
    }
}
