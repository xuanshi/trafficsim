/*
 * Copyright (C) 2014 Xuan Shi
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 */
jQuery(document).ready(function() {

	/***************************************************************************
	 * Menu, User Interface
	 **************************************************************************/
	/* network */
	jQuery('#user-interface-network').click(function() {
		simwebhelper.getPanel('network/view', function() {
			simwebhelper.getJson('map/center', function(data) {
				if (data['center'] != null)
					map.setCenter(data['center'], map.numZoomLevels);
			});
		});
	});

	/* draw */
	jQuery('#user-interface-draw').click(function() {
		simwebhelper.hidePanel();
		simulation.drawLinks();
	});

	/* links */
	jQuery('#user-interface-link').click(function() {
		simwebhelper.hidePanel();
		simulation.editLinks();
	});

	/* lanes */
	jQuery('#user-interface-lane').click(function() {
		simwebhelper.hidePanel();
		simulation.editLanes();
	});

	/* list */
	jQuery('#user-interface-list').click(function() {
		jsonEditor.open();
	});

	/* types */
	jQuery('#user-interface-types').click(function() {
		simwebhelper.getPanel('types/view');
	});

	/* vehicle composition */
	jQuery('#user-interface-compositions').click(function() {
		simwebhelper.getPanel('compositions/view');
	});
	/* plugin types */
	jQuery('#user-interface-plugin-types').click(function() {
		simwebhelper.getPanel('plugin/types');
	});

	/* plugin manager */
	jQuery('#user-interface-plugin-manager').click(function() {
		simwebhelper.getPanel('plugin/manager');
	});

	/* simulator */
	jQuery('#user-interface-simulator').click(function() {
		simwebhelper.getPanel('simulator/view');
	});

	/* simulation result */
	jQuery('#user-interface-results').click(function() {
		simwebhelper.getPanel('results/view');
	});

	/* Create New Scenario */
	jQuery('#user-interface-scenario').click(function() {
		simwebhelper.getPanel('scenario/new');
	});

	/* json editor example */
	jQuery('#user-interface-xxx').click(function() {
		/*
		 * jQuery('#user-editor').show(); var container =
		 * document.getElementById("user-json-editor"); var editor = new
		 * jsoneditor.JSONEditor(container); // set json var json = { "link1" : {
		 * "StartNode" : "One", "EndNode" : "Another", "Lane" : { "width" : 3,
		 * "start" : 0, "end" : 12, "shift" : 6 } } }; editor.set(json); // get
		 * json var json = editor.get();
		 */
	});

	/***************************************************************************
	 * Test
	 **************************************************************************/
	// TEST HACK TODO
	jQuery('#test').click(function() {
		jQuery.get('getdemonetwork', function(data) {
			data = eval('(' + data + ')');

			simulation.reDrawAllFeatures(data);
			var center = new OpenLayers.LonLat(data.center[0], data.center[1]);
			map.setCenter(center, map.numZoomLevels);
		});
	});
	jQuery('#test-anim').click(function() {
		simulation.load();
	});
});