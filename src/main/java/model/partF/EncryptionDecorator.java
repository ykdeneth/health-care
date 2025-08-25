
package model.partF;

import java.util.Base64;

/**
 *
 * @author Deneth
 */
public class EncryptionDecorator extends DataServiceDecorator {

    public EncryptionDecorator(DataService wrappee) {
        super(wrappee);
    }

    @Override
    public byte[] getData() {
        byte[] encrypted = wrappee.getData();
        return decrypt(encrypted);
    }

    @Override
    public void saveData(byte[] data) {
        byte[] encrypted = encrypt(data);
        wrappee.saveData(encrypted);
    }

    private byte[] encrypt(byte[] data) {
        // Simple Base64 for demo (replace with real encryption)
        return Base64.getEncoder().encode(data);
    }

    private byte[] decrypt(byte[] encrypted) {
        return Base64.getDecoder().decode(encrypted);
    }
}
