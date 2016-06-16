package org.n52.iceland.ogc.ows;

import java.util.Optional;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class OwsKeyword {

    private final OwsLanguageString keyword;
    private final Optional<OwsCodeType> type;

    public OwsKeyword(OwsLanguageString keyword, OwsCodeType type) {
        this.keyword = keyword;
        this.type = Optional.ofNullable(type);
    }

    public OwsKeyword(OwsLanguageString keyword) {
        this(keyword, null);
    }

    public OwsKeyword(String keyword) {
        this(keyword, null);
    }

    public OwsKeyword(String keyword, OwsCodeType type) {
        this(new OwsLanguageString(keyword), type);
    }

    public OwsLanguageString getKeyword() {
        return keyword;
    }

    public Optional<OwsCodeType> getType() {
        return type;
    }

}
