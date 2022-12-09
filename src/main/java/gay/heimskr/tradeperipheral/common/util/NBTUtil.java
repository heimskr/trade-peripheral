package gay.heimskr.tradeperipheral.common.util;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import gay.heimskr.tradeperipheral.TradePeripheral;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.*;
import net.minecraft.world.level.ChunkPos;
import org.apache.logging.log4j.Level;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import java.util.Map;

public class NBTUtil {

    public static Tag toDirectNBT(Object object) {
        // Mostly dan200.computercraft.shared.util toNBTTag method
        // put this map storing changes
        // instead of map serialization use direct map as CompoundNBT
        // assuming that map keys are strings
        if (object == null) {
            return null;
        } else if (object instanceof Boolean bool) {
            return ByteTag.valueOf((byte) (bool ? 1 : 0));
        } else if (object instanceof Integer integer) {
            return IntTag.valueOf(integer);
        } else if (object instanceof Number number) {
            return DoubleTag.valueOf(number.doubleValue());
        } else if (object instanceof String) {
            return StringTag.valueOf(object.toString());
        } else if (object instanceof Map<?, ?> map) {
            CompoundTag nbt = new CompoundTag();

            for (Map.Entry<?, ?> item : map.entrySet()) {
                Tag value = toDirectNBT(item.getValue());
                if (item.getKey() != null && value != null) {
                    nbt.put(item.getKey().toString(), value);
                }
            }
            return nbt;
        } else {
            return null;
        }
    }

    public static CompoundTag fromText(String json) {
        try {
            return json == null ? null : TagParser.parseTag(json);
        } catch (CommandSyntaxException ex) {
            TradePeripheral.debug("Could not parse json data to NBT", Level.ERROR);
            ex.printStackTrace();
            return null;
        }
    }

    public static CompoundTag fromBinary(String base64) {
        if (base64 == null) return null;

        try (InputStream inputStream = Base64.getDecoder().wrap(new ByteArrayInputStream(base64.getBytes()))) {
            return NbtIo.readCompressed(inputStream);
        } catch (IOException ex) {
            TradePeripheral.debug("Could not parse binary data to NBT", Level.ERROR);
            ex.printStackTrace();
            return null;
        }
    }

    public static CompoundTag toNBT(BlockPos pos) {
        CompoundTag data = new CompoundTag();
        data.putInt("x", pos.getX());
        data.putInt("y", pos.getY());
        data.putInt("z", pos.getZ());
        return data;
    }

    public static BlockPos blockPosFromNBT(CompoundTag nbt) {
        return new BlockPos(nbt.getInt("x"), nbt.getInt("y"), nbt.getInt("z"));
    }

    public static CompoundTag toNBT(ChunkPos pos) {
        CompoundTag data = new CompoundTag();
        data.putInt("x", pos.x);
        data.putInt("z", pos.z);
        return data;
    }

    public static ChunkPos chunkPosFromNBT(CompoundTag nbt) {
        return new ChunkPos(nbt.getInt("x"), nbt.getInt("z"));
    }
}
