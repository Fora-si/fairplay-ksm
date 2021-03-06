package ru.devinside.drm.fairplay.ksm.ckc;

import ru.devinside.drm.fairplay.ksm.common.TllvBlock;
import ru.devinside.drm.fairplay.ksm.common.TllvBlockSerializer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class CkcPayloadSerializer {
    private final CkcPayload ckcPayload;

    public CkcPayloadSerializer(CkcPayload ckcPayload) {
        this.ckcPayload = ckcPayload;
    }

    public byte[] serialize() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        serializeContentKeyTllv(out);
        serializeR1Tllv(out);
        serializeContenKeyDurationTllv(out);
        serializeReturnRequesTllvs(out);

        return out.toByteArray();
    }

    private void serializeContentKeyTllv(ByteArrayOutputStream out) throws IOException {
        out.write(ByteBuffer.allocate(8).putLong(CkcTag.ENCRYPTED_CK.getTag()).array());
        out.write(ByteBuffer.allocate(4).putInt(48).array()); // Block length: Value(32) + Padding(16)
        out.write(ByteBuffer.allocate(4).putInt(32).array()); // Value length: IV(16) + CK(16)
        out.write(ckcPayload.getCkcDataIv().getIv());
        out.write(ckcPayload.getEncryptedCk().getEncryptedCk());
        try {
            out.write(SecureRandom.getInstanceStrong().generateSeed(16));
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e);
        }
    }

    private void serializeR1Tllv(ByteArrayOutputStream out) throws IOException {
        out.write(
                new TllvBlockSerializer(
                        new TllvBlock(CkcTag.R1.getTag(), ckcPayload.getR1().getR1())
                ).serialize()
        );
    }

    private void serializeContenKeyDurationTllv(ByteArrayOutputStream out) throws IOException {
        // TODO: Not supported yet
        // TODO: Use ckcPayload.getCkcContentKeyDuration()
        // TODO: out.write(...);
    }

    private void serializeReturnRequesTllvs(ByteArrayOutputStream out) throws IOException {
        for (TllvBlock tllvBlock : ckcPayload.getReturnRequestBlocks()) {
            out.write(new TllvBlockSerializer(tllvBlock).serialize());
        }
    }
}
