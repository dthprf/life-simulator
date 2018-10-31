package com.codecool.Model;

import com.codecool.Model.MobData.MobData;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ComponentContainer {

    private final SynchronizedContainer container;

    public ComponentContainer(List<MobData> mobs, List<Resource> resources) {
        this.container = new SynchronizedContainer(mobs, resources);
    }

    public void addMob(MobData mob) {
        synchronized (container) {
            container.addMob(mob);
        }
    }

    public void removeMob(MobData mob) {
        synchronized (container) {
            container.removeMob(mob);
        }
    }

    public void addResource(Resource resource) {
        synchronized (container) {
            container.addResource(resource);
        }
    }

    public synchronized boolean removeResource(Resource resource) {
        synchronized (container) {
            return container.removeResource(resource);
        }
    }

    public synchronized Resource removeResourceOfType(String type) {
        synchronized (container) {
            return container.removeResourceOfType(type);
        }
    }

    public boolean hasMobs() {
        synchronized (container) {
            return container.hasMobs();
        }
    }

    public boolean hasMobsOfType(String breed) {
        synchronized (container) {
            return container.hasMobsOfType(breed);
        }
    }

    public boolean hasResourceOfType(String name) {
        synchronized (container) {
            return container.hasResourceOfType(name);
        }
    }

    public MobData getBestPrey(MobData hunter) {
        synchronized (container) {
            return container.getBestPrey(hunter);
        }
    }

    public List<MobData> getMobs() {
        synchronized (container) {
            return container.getMobs();
        }
    }

    public Resource getBestFood(String[] foodList) {
        synchronized (container) {
            return container.getBestFood(foodList);
        }
    }

    public List<Resource> getResources() {
        synchronized (container) {
            return container.getResources();
        }
    }

    public String asChar() {
        return container.asChar();
    }
}
