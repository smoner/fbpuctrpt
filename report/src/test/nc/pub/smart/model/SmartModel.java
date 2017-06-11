package nc.pub.smart.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.bs.framework.comn.NetStreamConstants;
import nc.pub.smart.cache.SmartDefCache;
import nc.pub.smart.context.InitialContextFactory;
import nc.pub.smart.context.SmartContext;
import nc.pub.smart.data.DataSet;
import nc.pub.smart.data.DbTable;
import nc.pub.smart.data.SmartModelData;
import nc.pub.smart.exception.SmartException;
import nc.pub.smart.exception.SmartValidateException;
import nc.pub.smart.metadata.MetaData;
import nc.pub.smart.model.descriptor.Descriptor;
import nc.pub.smart.model.preferences.MacroVariable;
import nc.pub.smart.model.preferences.Parameter;
import nc.pub.smart.model.preferences.Preferences;
import nc.pub.smart.provider.Provider;
import nc.pub.smart.script.validate.CheckResult;
import nc.pub.smart.script.validate.SmartValidateProcessor;
import nc.pub.smart.script.validate.SmartValidatorManager;
import nc.pub.smart.util.SmartUtilities;

import com.ufida.dataset.IContext;
import com.ufida.iufo.pub.tools.AppDebug;
import com.ufida.iufo.pub.tools.DeepCopyUtilities;
import com.ufida.zior.exception.MessageException;
import com.ufida.zior.perfwatch.PerfWatch;

/**
 * ����ģ��
 * 
 * ����㶨��ģ��
 * 
 * @author liuyy
 * 
 */
public final class SmartModel implements Cloneable, Serializable {

	private static final long serialVersionUID = 6059147478278114036L;

	/**
	 * ������Դ
	 */
	private Provider[] providers = new Provider[0];

	/**
	 * Ԫ����
	 */
	private MetaData m_metaData = new MetaData();// ����Ӧ���еķǿ��ж�

	/**
	 * ����������
	 */
	private Descriptor[] descriptors = new Descriptor[0];

	/**
	 * ��ѡ��
	 */
	private Preferences preferences = new Preferences();

	/**
	 * �ű�ģ��
	 */
	private SmartScriptModel scriptModel = null;// ȡ��transient������ʹ�õ��Ĳ����������������ģ�͵ȣ�����Ч��

	/**
	 * ����ģ��id
	 */
	private String id = null;

	/**
	 * ��չ���� �Զ�����򵼵�
	 */
	private Map<String, Object> m_extProp = null;

	public SmartModel() {

	}

	public SmartModel(String id) {
		setId(id);
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	SmartScriptModel getScriptModel() {
		if (scriptModel == null) {
			try {
				scriptModel = new SmartScriptModel(this);
			} catch (Exception e) {
				// AppDebug.debug(e);
				// throw new MessageException("����ű�ģ�ͳ�ʼ��ʧ�ܣ�" + e.getMessage(),
				// e);
				throw new MessageException(e.getMessage(), e);
			}
		}
		return scriptModel;
	}

	/**
	 * ģ���޸ĺ�һ��Ҫ����ű�ģ��
	 */
	public void cleanScriptModel() {
		scriptModel = null;
	}

	/**
	 * ����ִ��
	 * 
	 * @param descs
	 *            ����ʱ������
	 * @return ���ݼ�
	 */
	public DataSet provideData(IContext context, Descriptor[] descs)
			throws SmartException {

		if (SmartUtilities.isRunningInBrowser()) {
			final boolean STREAM_NEED_COMPRESS = NetStreamConstants.STREAM_NEED_COMPRESS;
			try {
				NetStreamConstants.STREAM_NEED_COMPRESS = true;
				SmartModelData smd = genSmartModelData(context, descs);
				DataSet[] rslt = SmartUtilities.getSmartService().provideData(
						new SmartModelData[] { smd });
				return rslt[0];
			} finally {
				NetStreamConstants.STREAM_NEED_COMPRESS = STREAM_NEED_COMPRESS;
			}
		}

		String tag = "SmartModel.provideData: ";
		if (getId() != null) {
			tag += SmartDefCache.getInstance().getNameWithCode(getId());
		}
		PerfWatch pw = new PerfWatch(tag);
		try {
			DataSet ds = getStrategy().provideData(context, descs);
			return ds;
		} finally {
			pw.stop();
		}
	}

	/**
	 * У������ģ�ͺϷ���
	 * 
	 * @throws SmartValidateException
	 */
	public void validate() throws SmartValidateException {

		// ����У����У��
		SmartValidateProcessor validator = new SmartValidateProcessor(
				SmartValidatorManager.getInstance());
		List<CheckResult> checkResultList = validator.validate(this);
		if (checkResultList != null && checkResultList.size() > 0) {
			throw new SmartValidateException(checkResultList);
		}
	}

	/**
	 * �ṩ��������
	 * 
	 * @param descs
	 *            ����ʱ������
	 * @return ��������
	 */
	public int provideCount(IContext context, Descriptor[] descs)
			throws SmartException {
		if (SmartUtilities.isRunningInBrowser()) {
			SmartModelData smd = genSmartModelData(context, descs);
			Integer[] rslt = SmartUtilities.getSmartService().provideCount(
					new SmartModelData[] { smd });
			return rslt[0];
		}
		// validate();
		String tag = "SmartModel.provideCount: ";
		if (getId() != null) {
			tag += SmartDefCache.getInstance().getNameWithCode(getId());
		}
		PerfWatch pw = new PerfWatch(tag);
		try {
			return getStrategy().provideCount(context, descs);
		} finally {
			pw.stop();
		}

	}

	/**
	 * ִ��SQL �Ѵ������ݼӹ����ﻯ����
	 * 
	 * @param context
	 * @param descs
	 * @return
	 * @throws Exception
	 */
	public String provideSQL(IContext context, Descriptor[] descs)
			throws SmartException {

		if (SmartUtilities.isRunningInBrowser() && getId() != null) {
			SmartModelData smd = genSmartModelData(context, descs);
			String[] rslt = SmartUtilities.getSmartService().provideSQL(
					new SmartModelData[] { smd });
			return rslt[0];
		}
		String tag = "SmartModel.provideSQL: ";
		if (getId() != null) {
			tag += SmartDefCache.getInstance().getNameWithCode(getId());
		}
		PerfWatch pw = new PerfWatch(tag);
		try {
			return getStrategy().provideSQL(context, descs);
		} finally {
			pw.stop();
		}
	}

	/**
	 * ִ�н����������ͼ��
	 * 
	 * @param descs
	 *            ����ʱ������
	 * @return ��ͼ��װ
	 */
	public DbTable provideView(IContext context, Descriptor[] descs)
			throws SmartException {

		if (SmartUtilities.isRunningInBrowser()) {
			SmartModelData smd = genSmartModelData(context, descs);
			DbTable[] rslt = SmartUtilities.getSmartService().provideView(
					new SmartModelData[] { smd });
			return rslt[0];
		}

		String tag = "SmartModel.provideView: ";
		if (getId() != null) {
			tag += SmartDefCache.getInstance().getNameWithCode(getId());
		}
		PerfWatch pw = new PerfWatch(tag);
		try {
			return getStrategy().provideView(context, descs);
		} finally {
			pw.stop();
		}
	}

	private SmartModelData genSmartModelData(IContext context,
			Descriptor[] descs) {
		SmartModelData smd = null;
		if (getId() != null) {
			smd = new SmartModelData(getId(), context, descs);
		} else {
			smd = new SmartModelData(this, context, descs);
		}
		return smd;
	}

	public void setMetaData(MetaData meta) {
		if (meta == null) {
			throw new IllegalArgumentException("meatData is null.");
		}
		this.m_metaData = meta;
	}

	public MetaData getMetaData() {
		return m_metaData;
	}

	public void setDescriptors(Descriptor[] descriptors) {
		this.descriptors = descriptors;
	}

	public Descriptor[] getDescriptors() {
		return descriptors;
	}

	public void setPreferences(Preferences preferences) {
		if (preferences == null)
			throw new IllegalArgumentException();
		this.preferences = preferences;
	}

	public Preferences getPreferences() {
		return preferences;
	}

	/**
	 * ����ָ�����͵������� ��Ϊ�����������ظ�����������
	 */
	public Descriptor[] getDescriptor(String clzName) {
		ArrayList<Descriptor> list = new ArrayList<Descriptor>(0);
		for (Descriptor d : descriptors) {
			if (d.getClass().getName().equals(clzName)) {
				list.add(d);
			}
		}
		return list.toArray(new Descriptor[0]);
	}

	@SuppressWarnings("unchecked")
	public Object clone() {
		try {
			SmartModel copy = (SmartModel) super.clone();

			copy.descriptors = (Descriptor[]) DeepCopyUtilities
					.getDeepCopy(this.descriptors);

			copy.providers = (Provider[]) DeepCopyUtilities
					.getDeepCopy(this.providers);

			if (this.m_metaData != null) {
				copy.m_metaData = (MetaData) this.getMetaData().clone();
			}

			if (this.preferences != null) {
				copy.preferences = (Preferences) this.preferences.clone();
			}

			if (m_extProp != null) {
				copy.m_extProp = (Map<String, Object>) DeepCopyUtilities
						.getDeepCopy(this.m_extProp);
			}

			copy.scriptModel = null;

			return copy;

		} catch (Exception e) {
			AppDebug.debug(e);
			throw new RuntimeException(e);
		}
	}

	/**
	 * ����ģ��ִ������Դ ִ������Դ�����߼����£� 1.����������ﻯ���ԣ���ָ�����ﻯ����Դ�������ﻯ����Դ
	 * 2.���������ָ����ִ������Դ�����ģ���κγ����¶��ø�����Դִ�У���ʹ���ϵ�����ģ���У�
	 * 3.δָ��ִ������Դ��ʹ���������е�ִ������Դ���������е�����Դһ���Ƕ�������ģ�͵�ִ������Դ�����ָ����
	 * 4.δ����ִ������Դ����������δָ��ִ������Դ��ʹ�õ�ǰ����Դ
	 * 
	 * @return
	 */
	public String provideDsName4Exec(IContext context) {
		return getStrategy().provideDsName4Exec(context);
	}

	public Provider[] getProviders() {
		if (providers == null) {
			return new Provider[0];
		}
		return providers;
	}

	public Provider getProvider(String code) {
		Provider[] prvds = getProviders();
		for (Provider p : prvds) {
			if (p.getCode().equals(code)) {
				return p;
			}

		}
		return null;
	}

	public String[] getRefModelIds() {
		return getScriptModel().getRefModelIds();
	}

	public void setProviders(Provider[] providers) {
		this.providers = providers;
	}

	/**
	 * ʹ�õ��Ĳ��� �������õ�����ģ��ʹ�õ��Ĳ���
	 * 
	 * @return
	 */
	public Parameter[] getUsedParams() {
		try {
			return getScriptModel().getUsedParams();
		} catch (SmartException e) {
			throw new MessageException(e.getMessage(), e);
		}
	}

	/**
	 * ʹ�õ��ĺ���� �������õ�����ģ��ʹ�õ��ĺ����
	 * 
	 * @return
	 */
	public MacroVariable[] getUsedMacroVars() {
		try {
			return getScriptModel().getUsedMacroVars();
		} catch (SmartException e) {
			throw new MessageException(e.getMessage(), e);
		}
	}

	private ISmartExecStrategy getStrategy() {
		return ExecStrategyFactory.getExecStrategy(this);
	}

	public Object getExtProp(String key) {

		return getExtProp().get(key);
	}

	public void putExtProp(String key, Object object) {
		getExtProp().put(key, object);
	}

	public void removeExtProp(String key) {

		getExtProp().remove(key);
	}

	private Map<String, Object> getExtProp() {
		if (m_extProp == null) {
			m_extProp = new HashMap<String, Object>();
		}
		return m_extProp;
	}

	/**
	 * ��ȡ��ʼ������ 1.ִ�к������ 2.��������Ĭ��ֵ;
	 * 
	 * @return
	 * @throws SmartException
	 */
	public SmartContext getInitialContext() throws SmartException {
		return InitialContextFactory.getInitialContext(this);
	}

}
