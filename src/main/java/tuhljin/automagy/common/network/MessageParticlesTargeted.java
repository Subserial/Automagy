package tuhljin.automagy.common.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;
import thaumcraft.common.lib.SoundsTC;
import tuhljin.automagy.common.lib.ThaumcraftExtension;

public class MessageParticlesTargeted extends BlockTiedMessageToClient<MessageParticlesTargeted> {
    public static final short BOLT_BLACK = 0;
    protected short id;
    protected int targetX;
    protected int targetY;
    protected int targetZ;

    public MessageParticlesTargeted() {
    }

    public MessageParticlesTargeted(short id, int dim, int x, int y, int z, int targetX, int targetY, int targetZ) {
        super(dim, x, y, z);
        this.id = id;
        this.targetX = targetX;
        this.targetY = targetY;
        this.targetZ = targetZ;
    }

    public void fromBytes(ByteBuf buf) {
        super.fromBytes(buf);
        this.id = buf.readShort();
        this.targetX = buf.readInt();
        this.targetY = buf.readInt();
        this.targetZ = buf.readInt();
    }

    public void toBytes(ByteBuf buf) {
        super.toBytes(buf);
        buf.writeShort(this.id);
        buf.writeInt(this.targetX);
        buf.writeInt(this.targetY);
        buf.writeInt(this.targetZ);
    }

    public void onReceived(World world, BlockPos pos) {
        switch(this.id) {
            case BOLT_BLACK:
                this.zapBlock(7, world, 0.1F, 0.0F, 0.8F);
                world.playSound(this.x + 0.5D, this.y + 0.5D, this.z + 0.5D, SoundsTC.zap, SoundCategory.BLOCKS, 0.2F, world.rand.nextFloat() * 0.5F + 0.4F, false);
                world.playSound(this.x + 0.5D, this.y + 0.5D, this.z + 0.5D, SoundEvents.ENTITY_GENERIC_EAT, SoundCategory.BLOCKS, 0.7F, world.rand.nextFloat() * 0.5F + 0.1F, false);
                world.playSound(this.x + 0.5D, this.y + 0.5D, this.z + 0.5D, SoundEvents.ENTITY_GENERIC_EAT, SoundCategory.BLOCKS, 0.5F, world.rand.nextFloat() * 0.5F + 1.0F, false);
            default:
        }
    }

    private void zapBlock(int num, World world, float r, float g, float b) {
        BlockPos source = new BlockPos(this.x, this.y, this.z);
        BlockPos target = new BlockPos(this.targetX, this.targetY, this.targetZ);

        for(int i = 0; i < num; ++i) {
            ThaumcraftExtension.zapBlock(world, source, target, r, g, b, 1.0F);
        }

    }

    public static void sendToClients(short id, World world, int x, int y, int z, int targetX, int targetY, int targetZ) {
        if (!world.isRemote) {
            int dim = world.provider.getDimension();
            TargetPoint point = new TargetPoint(dim, x, y, z, PacketHandler.DEFAULT_PACKET_RANGE);
            PacketHandler.INSTANCE.sendToAllAround(new MessageParticlesTargeted(id, dim, x, y, z, targetX, targetY, targetZ), point);
        }
    }

    public static void sendToClients(short id, World world, BlockPos pos, BlockPos targetPos) {
        sendToClients(id, world, pos.getX(), pos.getY(), pos.getZ(), targetPos.getX(), targetPos.getY(), targetPos.getZ());
    }
}

