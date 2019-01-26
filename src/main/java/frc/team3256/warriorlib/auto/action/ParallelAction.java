package frc.team3256.warriorlib.auto.action;

import java.util.ArrayList;
import java.util.List;

/**
 * Runs multiple actions at the same time. Finishes when every action is finished
 */
public class ParallelAction implements Action {
	private final ArrayList<Action> actions;

	public ParallelAction(List<Action> actions) {
		this.actions = new ArrayList<>(actions.size());
		this.actions.addAll(actions);
	}

	@Override
	public boolean isFinished() {
		for (Action action : actions)
			if (!action.isFinished())
				return false;
		return true;
	}

	@Override
	public void update() {
		for (Action action : actions)
			action.update();
	}

	@Override
	public void done() {
		for (Action action : actions)
			action.done();
	}

	@Override
	public void start() {
		for (Action action : actions)
			action.start();
	}
}
