package no.nsd.qddt.domain.comment;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Stig Norland
 */
public class ContentJson {
    private PageJson page;
    private Set<ContentJson> content = new HashSet<>();

    public ContentJson(Comment content) {
        this.content = content.getChildren().stream().map(f->new ContentJson(f)).collect(Collectors.toSet());
        page = new PageJson(content.getTreeSize());
    }

    public PageJson getPage() {
        return page;
    }

    public void setPage(PageJson page) {
        this.page = page;
    }

    public Set<ContentJson> getContent() {
        return content;
    }

    public void setContent(Set<ContentJson> content) {
        this.content = content;
    }

    public class PageJson {
        private int treeSize;

        public PageJson(int treeSize) {
            this.treeSize = treeSize;
        }

        public int getTreeSize() {
            return treeSize;
        }

        public void setTreeSize(int treeSize) {
            this.treeSize = treeSize;
        }
    }
}
