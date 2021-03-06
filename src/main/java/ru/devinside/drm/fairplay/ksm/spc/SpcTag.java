package ru.devinside.drm.fairplay.ksm.spc;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum SpcTag {
    // A combination of values that the KSM will use to encrypt the content key and the CKC payload.
    // The R1 value must be returned to FPS in the payload of the CKC message.
    SK_R1(0x3d1a10b8bffac2ecL),

    // A 16-byte value used to check the integrity of the contents of the [SK...R1] block.
    SK_R1_INTEGRITY(0xb349d4809e910687L),

    // Anti-replay seed.
    // A 16-byte value used in the encryption of the CKC payload.
    AR_SEED(0x89c90f12204106b2L),

    // A 21-byte value used in decrypting the payload of the [SK...R1] block.
    R2(0x71b5595ac1521133L),

    // A TLLV block that contains zero or more concatenated 8-byte values,
    // each of which is the tag for a different TLLV block in the SPC.
    // All of the TLLV blocks listed must be retrieved and returned as is in the CKC payload.
    RETURN_REQUEST(0x19f9d4e5ab7609cbL),

    // A content provider ID that tells the key server which content needs to be decrypted.
    // This value may be generated by the playback app or created by the FPS implementer.
    // Its length can range from 2 to 200 bytes, inclusive. The asset ID content must be padded
    // to a multiple of 16 bytes, regardless of the original length.
    ASSET_ID(0x1bf7f53f5d5d5a1fL),

    // An 8-byte value that identifies the current FPS transaction. The KSM does not need to process this information.
    TRANSACTION_ID(0x47aa7ad3440577deL),

    // A concatenation of 4-byte values identifying the Apple device-supported versions of FPS.
    PROTOCOL_VERSIONS_SUPPORTED(0x67b8fb79ecce1a13L),

    // A 4-byte value that identifies the version of FPS that the Apple device is using for this FPS transaction.
    PROTOCOL_VERSION_USED(0x5d81bcbcc7f61703L),

    // A single 8-byte value. Indicates to the server that the content will be sent by AirPlay to an Apple TV box.
    // Any other value means that content playback will occur on the requesting device.
    STREAMING_INDICATOR_AIRPLAY_TO_APPLE_TV(0xabb0256a31843974L),

    // A single 8-byte value. Indicates to the server that the content will be sent to an Apple digital AV adapter.
    STREAMING_INDICATOR_APPLE_DIGITAL_AV_ADAPTER(0x5f9c8132b59f2fdeL),

    // Media playback information for rental and lease.
    MEDIA_PLAYBACK_STATE(0xeb8efdf2b25ab3a0L),

    UNIDENTIFIED_TAG(0);

    private final long tag;

    private final static Map<Long, SpcTag> spcTagByLongValue = Arrays.stream(SpcTag.values()).collect(
            Collectors.toMap(SpcTag::getTag, Function.identity())
    );

    SpcTag(long tag) {
        this.tag = tag;
    }

    public long getTag() {
        return tag;
    }

    public static SpcTag valueOf(long tag) {
        return spcTagByLongValue.getOrDefault(tag, UNIDENTIFIED_TAG);
    }
}
