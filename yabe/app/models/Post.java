package models;

import java.util.*;

import javax.persistence.*;

import play.db.jpa.*;
import play.data.validation.*;

@Entity
public class Post extends Model{
	@Required
	public String title;
	@Required
    public Date postedAt;
    
    /* ATENÇÃO: Temos uma dupla referencia entre post e comment
     * O mappedBy diz ao JPA quem é o dono da relação. No caso é o post.
    // cascade determina que se um post for deletado, todos os seus
     * comentarios tmb serão.
     * */ 
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    public List<Comment> comments;
    
    @Lob
    @Required
    @MaxSize(10000)
    public String content;
    
    @Required
    @ManyToOne
    public User author;
    
    @ManyToMany(cascade=CascadeType.PERSIST)
    public Set<Tag> tags;
    
    public Post(User author, String title, String content) {
        this.comments = new ArrayList<Comment>();
        this.tags = new TreeSet<Tag>();
        this.author = author;
        this.title = title;
        this.content = content;
        this.postedAt = new Date();
    }
    
    public Post addComment( String author, String content ) {
    	Comment newComment = new Comment( this, author, content).save(); //atencao aqui: operacao com BD
    	this.comments.add(newComment);
    	this.save();
    	return this;
    }
    
    public Post previous() {
    	return Post.find("postedAt > ? order by postedAt desc", postedAt).first();
    }
    
    public Post next(){
    	return Post.find("postedAt > ? order by postedAt asc", postedAt).first();
    }
    
    public Post tagItWith(String name) {
        tags.add(Tag.findOrCreateByName(name));
        return this;
    }
    
    public static List<Post> findTaggedWith(String... tags) {
        return Post.find(
                "select distinct p from Post p join p.tags as t where t.name in (:tags) group by p.id, p.author, p.title, p.content,p.postedAt having count(t.id) = :size"
        ).bind("tags", tags).bind("size", tags.length).fetch();
    }
    
    @Override
    public String toString() {
    	// TODO Auto-generated method stub
    	return title;
    }

}
