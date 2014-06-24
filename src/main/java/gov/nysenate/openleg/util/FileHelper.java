package gov.nysenate.openleg.util;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.*;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class FileHelper
{
    /**
     * Returns a collection of files sorted by file name (not file path!)
     *
     * @param directory - The directory to get files from.
     * @param recursive - true to retrieve files from sub-directories.
     * @return Collection<File>
     * @throws java.io.IOException
     */
    public static Collection<File> getSortedFiles(File directory, boolean recursive, String[] excludeDirs)
            throws IOException {
        Collection<File> files = safeListFiles(directory, recursive, excludeDirs);
        Collections.sort((List<File>) files, new Comparator<File>() {
            public int compare(File a, File b) {
                return a.getName().compareTo(b.getName());
            }
        });
        return files;
    }

    /**
     * Any directory that we attempt to list files from should exist. If it doesn't then
     * create them. This makes the processes robust against incomplete environment setups.
     *
     * @param directory - The directory to list files from (and create if necessary)
     * @param extensions - A list of extensions to grab. null for all extensions.
     * @param recursive - true when you want to list files recursively.
     * @return A collection of matching filenames
     * @throws IOException
     */
    public static Collection<File> safeListFiles(File directory, String[] extensions, boolean recursive) throws IOException {
        FileUtils.forceMkdir(directory);
        return FileUtils.listFiles(directory, extensions, recursive);
    }

    /**
     * Any directory that we attempt to list files from should exist. If it doesn't then
     * create them. This overload can be used to exclude certain directories if recursive is true.
     *
     * @param directory - The directory to list files from (and create if necessary)
     * @param recursive - true when you want to list files recursively.
     * @param excludeDirs - Array of directory names that should be excluded from the match.
     *                      Set to null if all directories should be matched.
     * @return A collection of matching filenames
     * @throws IOException
     */
    public static Collection<File> safeListFiles(File directory, boolean recursive, String[] excludeDirs) throws IOException {
        FileUtils.forceMkdir(directory);
        IOFileFilter dirFileFilter;
        if (excludeDirs != null && excludeDirs.length > 0) {
            List<IOFileFilter> excludeDirFilters = new ArrayList<>();
            for (String excludeDir : excludeDirs) {
                excludeDirFilters.add(FileFilterUtils.nameFileFilter(excludeDir));
            }
            dirFileFilter = FileFilterUtils.notFileFilter(new OrFileFilter(excludeDirFilters));
        }
        else {
            dirFileFilter = (recursive) ? TrueFileFilter.TRUE : FalseFileFilter.FALSE;
        }
        return FileUtils.listFiles(directory, TrueFileFilter.TRUE, dirFileFilter);
    }

    /**
     * Create the specified folder if necessary and return a File handle to it.
     *
     * @param parent - The parent directory for this folder
     * @param folderName - The name of the directory to retrieve
     * @return a File handle to the requested folder.
     * @throws IOException
     */
    public static File safeGetFolder(File parent, String folderName) throws IOException {
        File directory = new File(parent, folderName);
        FileUtils.forceMkdir(directory);
        return directory;
    }

    /**
     *
     * @param file File
     * @param directory File
     * @param createDirectory ?
     * @throws IOException
     */
    public static void moveFileToDirectory(File file, File directory, boolean createDirectory) throws IOException {
        File newFile = new File(directory, file.getName());
        if (newFile.exists()) {
            newFile.delete();
        }
        FileUtils.moveFileToDirectory(file, directory, true);
    }
}
