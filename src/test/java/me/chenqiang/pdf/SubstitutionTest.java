package me.chenqiang.pdf;

import java.util.Map;

import org.junit.Test;

import me.chenqiang.pdf.utils.Substitution;

public class SubstitutionTest {
	@Test
	public void testSubstitution() {
		String text = "\\${保留字}$不必说${不必说一}${Test}；"
				+ "也不必说${不必说二}。"
				+ "单是周围的短短的泥墙根一带，就有无限趣味。"
				+ "${}${不保留}\\${保留字}";
		
		Map<String, String> params = Map.ofEntries(
				Map.entry("不必说一", "碧绿的菜畦，光滑的石井栏，高大的皂荚树，紫红的桑椹"),
				Map.entry("不必说二", "鸣蝉在树叶里长吟，肥胖的黄蜂伏在菜花上，轻捷的叫天子（云雀）忽然从草间直窜向云霄里去了"),
				Map.entry("Test", "Hello world!")
				);
		System.out.println(Substitution.substitute(text, params));
	}
}
