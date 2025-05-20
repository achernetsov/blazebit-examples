package com.example;

import com.blazebit.persistence.CriteriaBuilderFactory;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Tuple;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import java.util.List;

@Path("/items")
public class ItemsResource {

    @Inject
    protected EntityManager em;

    @Inject
    protected CriteriaBuilderFactory cbf;

//    @GET
//    @Produces(MediaType.TEXT_PLAIN)
//    public String hello() {
//
//        return "Hello from Quarkus REST";
//    }

    @POST
    @Path("init")
    @Transactional
    public void init() {

        var item = new Item();
        item.id = 1;
        item.model = "M1";
        item.owner = "D1";
        em.persist(item);

        item = new Item();
        item.id = 2;
        item.model = "M1";
        item.owner = "D1";
        em.persist(item);

        item = new Item();
        item.id = 3;
        item.model = "M2";
        item.owner = "M1";
        em.persist(item);

        item = new Item();
        item.id = 4;
        item.model = "M2";
        item.owner = "M2";
        em.persist(item);
    }

    @GET
    public List<Item> getItems() {

        return cbf.create(em, Item.class)
            .getResultList();
    }

    @GET
    @Path("cte")
    public List<Tuple> cte() {
        // @formatter:off
        return cbf.create(em, Tuple.class)
            .with(ItemCTE.class)
                .from(Item.class,"i")
                .bind("id").select("i.id")
                .bind("model").select("i.model")
                .bind("owner").select("i.owner")
            .end()
            .from(ItemCTE.class,"c")
            .getResultList();
        // @formatter:on
    }

    @GET
    @Path("cteGroups")
    public List<Tuple> cteGroups() {
        // @formatter:off
        return cbf.create(em, Tuple.class)
            .with(ItemCTE.class)
                .from(Item.class,"i")
                .bind("id").select("i.id")
                .bind("model").select("i.model")
                .bind("owner").select("i.owner")
            .end()
            .from(ItemCTE.class,"c")
            .select("'model'","facet")
            .select("count(id)","value")
            .groupBy("model")
            .unionAll()
            .from(ItemCTE.class,"c")
            .select("'owner'","facet")
            .select("count(id)","value")
            .groupBy("owner")
            .endSet()
            .getResultList();
        // @formatter:on
    }
}
