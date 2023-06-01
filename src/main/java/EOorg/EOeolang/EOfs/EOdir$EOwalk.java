/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2021-2022 Yegor Bugayenko
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included
 * in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NON-INFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
// @checkstyle PackageNameCheck (1 line)
package EOorg.EOeolang.EOfs;

import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import org.eolang.AtComposite;
import org.eolang.AtFree;
import org.eolang.Data;
import org.eolang.Dataized;
import org.eolang.PhDefault;
import org.eolang.PhWith;
import org.eolang.Phi;
import org.eolang.XmirObject;

/**
 * Dir.walk.
 *
 * @since 0.1
 * @checkstyle TypeNameCheck (100 lines)
 * @checkstyle ClassDataAbstractionCouplingCheck (500 lines)
 */
@SuppressWarnings("PMD.AvoidDollarSigns")
@XmirObject(oname = "dir.walk")
public class EOdir$EOwalk extends PhDefault {

    /**
     * Ctor.
     * @param sigma The \sigma
     * @checkstyle BracketsStructureCheck (200 lines)
     */
    @SuppressWarnings("PMD.ConstructorOnlyInitializesOrCallOtherConstructors")
    public EOdir$EOwalk(final Phi sigma) {
        super(sigma);
        this.add("glob", new AtFree());
        this.add(
            "φ",
            new AtComposite(
                this,
                rho -> {
                    final Path path = Paths.get(
                        new Dataized(
                            rho.attr("ρ").get()
                        ).take(String.class)
                    ).toAbsolutePath();
                    final String glob = new Dataized(
                        rho.attr("glob").get()
                    ).take(String.class);
                    final PathMatcher matcher = FileSystems.getDefault().getPathMatcher(
                        String.format("glob:%s", glob)
                    );
                    final List<Phi> files = Files.walk(path)
                        .map(p -> p.toAbsolutePath().toString())
                        .map(p -> p.substring(p.indexOf(path.toString())))
                        .filter(p -> matcher.matches(Paths.get(p)))
                        .map(p -> new PhWith(new EOfile(sigma), 0, new Data.ToPhi(p)))
                        .collect(Collectors.toList());
                    return new Data.ToPhi(files.toArray(new Phi[] {}));
                }
            )
        );
    }

}
