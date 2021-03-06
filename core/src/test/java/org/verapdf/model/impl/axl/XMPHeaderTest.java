package org.verapdf.model.impl.axl;

import static org.junit.Assert.assertEquals;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.verapdf.pdfa.flavours.PDFAFlavour;

import com.adobe.xmp.XMPException;
import com.adobe.xmp.impl.VeraPDFMeta;

/**
 * @author Maksim Bezrukov
 */
@RunWith(Parameterized.class)
public class XMPHeaderTest {

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {"/org/verapdf/model/impl/axl/xmp-empty-rdf.xml", null, null},
                {"/org/verapdf/model/impl/axl/xmp-header-check-1.xml", "234", null},
                {"/org/verapdf/model/impl/axl/xmp-header-check-2.xml", "234", "UTF8"},
                {"/org/verapdf/model/impl/axl/xmp-header-check-3.xml", "234", "UTF8"},
                {"/org/verapdf/model/impl/axl/xmp-header-check-4.xml", null, null},
                {"/org/verapdf/model/impl/axl/xmp-header-check-5.xml", null, "UTF8"},
                {"/org/verapdf/model/impl/axl/xmp-header-check-6.xml", "234\"    encoding  =   \"UTF8", null}
        });
    }

    @Parameterized.Parameter
    public String filePath;

    @Parameterized.Parameter(value = 1)
    public String bytes;

    @Parameterized.Parameter(value = 2)
    public String encoding;

    @Test
    public void test() throws URISyntaxException, XMPException, IOException {
        try (FileInputStream in = new FileInputStream(getSystemIndependentPath(this.filePath))) {
        VeraPDFMeta meta = VeraPDFMeta.parse(in);
        AXLXMPPackage pack = new AXLXMPPackage(meta, true, null, PDFAFlavour.PDFA_1_B);
        assertEquals(this.bytes, pack.getbytes());
        assertEquals(this.encoding, pack.getencoding());
        }
    }

    private static String getSystemIndependentPath(String path)
            throws URISyntaxException {
        URL resourceUrl = ClassLoader.class.getResource(path);
        Path resourcePath = Paths.get(resourceUrl.toURI());
        return resourcePath.toString();
    }
}
