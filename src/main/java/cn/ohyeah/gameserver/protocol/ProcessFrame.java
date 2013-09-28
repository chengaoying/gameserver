package cn.ohyeah.gameserver.protocol;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;

public class ProcessFrame {
    private ByteBuf request;
    private Channel channel;

    public ByteBuf getRequest() {
        return request;
    }

    public void setRequest(ByteBuf request) {
        this.request = request;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

}
