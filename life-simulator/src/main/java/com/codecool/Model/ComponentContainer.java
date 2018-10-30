package com.codecool.Model;

import com.codecool.Model.MobData.MobData;

import java.util.List;

public class ComponentContainer {

    private List<MobData> mobs;
    private List<Resource> resources;

    public ComponentContainer(List<MobData> mobs, List<Resource> resources) {
        this.mobs = mobs;
        this.resources = resources;
    }

    public void addMob(MobData mob) {
        mobs.add(mob);
    }

    public void removeMob(MobData mob) {
        mobs.remove(mob);
    }

    public void addResource(Resource resource) {
        resources.add(resource);
    }

    public void removeResource(Resource resource) {
        resources.add(resource);
    }

    public boolean hasMobs() {
        return !mobs.isEmpty();
    }
}