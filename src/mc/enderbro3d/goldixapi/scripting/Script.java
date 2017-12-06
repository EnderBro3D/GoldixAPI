package mc.enderbro3d.goldixapi.scripting;

import java.io.File;

public class Script {
    private File file;

    public Script(File file) {
        this.file = file;
    }

    /**
     * Возвращает файл скрипта
     * @return Файл
     */
    public File getFile() {
        return file;
    }
}
