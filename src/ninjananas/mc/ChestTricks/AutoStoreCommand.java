package ninjananas.mc.ChestTricks;


import java.util.Vector;

import org.bukkit.FluidCollisionMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.RayTraceResult;

public class AutoStoreCommand implements CommandExecutor {
	
	public AutoStoreCommand(Plugin plugin) {
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String val, String[] args) {
		if (! (sender instanceof Player)) {
			sender.sendMessage("This command can only be used by players!");
			return false;
		}
		Player player = (Player) sender;
		Location location = player.getEyeLocation();
		
		Vector<Chest> chests = new Vector<Chest>();
		
		for (double i = -3.0; i < 4.0; i++) {
			for (double j = -3.0; j < 4.0; j++) {
				for (double k = -3.0; k < 4.0; k++) {
					Block block = location.clone().add(i, j, k).getBlock();
					if (block.getType() == Material.CHEST) {
						RayTraceResult rayTraceResult = player.getWorld().rayTraceBlocks(
								location,
								block.getLocation().clone().add(0.5, 0.5, 0.5).subtract(location).toVector(),
								20,
								FluidCollisionMode.NEVER,
								false);
						if ( (rayTraceResult != null) & (rayTraceResult.getHitBlock().equals(block)) )
							chests.add((Chest) block.getState());
					}
				}
			}
		}
		
		for (Chest chest: chests) {
			Inventory chestInventory = chest.getInventory();
			Inventory playerInventory = player.getInventory();
			
			ItemStack[] inventoryContent = playerInventory.getStorageContents().clone();
			
			for (int i = 0; i < inventoryContent.length; i++) {
				if (i < 9) continue; // Skip elements in action bar
				ItemStack stack = inventoryContent[i];
				if (stack == null) continue; // Skip empty slots
				if (! chestInventory.contains(stack.getType())) continue; // Only store items that are in the chest
				
				inventoryContent[i] = null;
				for (ItemStack failedStack: chestInventory.addItem(stack).values())
					inventoryContent[i] = failedStack;
			}
			playerInventory.setStorageContents(inventoryContent);
			player.updateInventory();
		}
		
		return true;
	}

}
