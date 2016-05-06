/*
 * Copyright (C) 2016 Daniel Saukel
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package io.github.dre2n.dungeonsxl.listener;

import io.github.dre2n.commons.util.messageutil.MessageUtil;
import io.github.dre2n.dungeonsxl.DungeonsXL;
import io.github.dre2n.dungeonsxl.event.requirement.*;
import io.github.dre2n.dungeonsxl.requirement.AwesomenessRequirement;
import io.github.dre2n.dungeonsxl.requirement.RequirementTypeCustom;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

/**
 * @author Daniel Saukel
 */
public class RequirementListener implements Listener {

    DungeonsXL plugin = DungeonsXL.getInstance();

    @EventHandler
    public void onCheck(RequirementCheckEvent event) {
        MessageUtil.log(plugin, "&b== " + event.getEventName() + "==");
        MessageUtil.log(plugin, "Requirement: " + event.getRequirement().getType());
    }

    @EventHandler
    public void onDemand(RequirementDemandEvent event) {
        MessageUtil.log(plugin, "&b== " + event.getEventName() + "==");
        MessageUtil.log(plugin, "Requirement: " + event.getRequirement().getType());
    }

    @EventHandler
    public void onRegistration(RequirementRegistrationEvent event) {
        MessageUtil.log(plugin, "&b== " + event.getEventName() + "==");
        MessageUtil.log(plugin, "Requirement: " + event.getRequirement().getType());

        if (event.getRequirement().getType() == RequirementTypeCustom.AWESOMENESS) {
            MessageUtil.log(plugin, "Registering an " + RequirementTypeCustom.AWESOMENESS + " requirement.");
            ((AwesomenessRequirement) event.getRequirement()).setAwesomenessLevel(5);
        }
    }

}
