package org.rhm.stock.handler.maint;

import java.util.Date;

public interface MaintHandler {

	public void prune(Date deleteBefore);
}
