package com.tonicsystems.doclet;

import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.List;

import com.sun.javadoc.*;
import com.sun.tools.doclets.standard.Standard;
import com.sun.tools.javadoc.Main;

public class ExcludeDoclet
{
  public static boolean validOptions(String[][] options, DocErrorReporter reporter)
  {
    return Standard.validOptions(options, reporter);
  }

  public static int optionLength(String option)
  {
    return Standard.optionLength(option);
  }

  public static boolean start(RootDoc root)
  {
    return Standard.start((RootDoc) process(root, RootDoc.class));
  }

  private static boolean exclude(Doc doc)
  {
    if (doc instanceof ProgramElementDoc)
    {
      if (((ProgramElementDoc) doc).containingPackage().tags("exclude").length > 0)
        return true;
    }
    return doc.tags("exclude").length > 0;
  }

  private static Object process(Object obj, Class expect)
  {
    if (obj == null)
      return null;
    Class cls = obj.getClass();
    if (cls.getName().startsWith("com.sun."))
    {
      return Proxy.newProxyInstance(cls.getClassLoader(), cls.getInterfaces(), new ExcludeHandler(obj));
    }
    else if (obj instanceof Object[])
    {
      Class componentType = expect.getComponentType();
      Object[] array = (Object[]) obj;
      List list = new ArrayList(array.length);
      for (int i = 0; i < array.length; i++)
      {
        Object entry = array[i];
        if ((entry instanceof Doc) && exclude((Doc) entry))
          continue;
        list.add(process(entry, componentType));
      }
      return list.toArray((Object[]) Array.newInstance(componentType, list.size()));
    }
    else
    {
      return obj;
    }
  }

  private static class ExcludeHandler implements InvocationHandler
  {
    private Object target;

    public ExcludeHandler(Object target)
    {
      this.target = target;
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable
    {
      if (args != null)
      {
        String methodName = method.getName();
        if (methodName.equals("compareTo") || methodName.equals("equals")
            || methodName.equals("overrides") || methodName.equals("subclassOf"))
        {
          args[0] = unwrap(args[0]);
        }
      }
      try
      {
        return process(method.invoke(target, args), method.getReturnType());
      }
      catch (InvocationTargetException e)
      {
        throw e.getTargetException();
      }
    }

    private Object unwrap(Object proxy)
    {
      if (proxy instanceof Proxy)
        return ((ExcludeHandler) Proxy.getInvocationHandler(proxy)).target;
      return proxy;
    }
  }
  
  public static void main(String[] s)
  {
    System.out.println(System.getProperty("java.version"));
    String name = ExcludeDoclet.class.getName();
    String[] args = {
            "-classpath","/Applications/eclipse/plugins/org.junit_4.11.0.v201303080030/junit.jar:../RiTa/bin:../RiTa/libs/core.jar:../RiTa/libs/junit.jar:twitter4j-core-4.0.2.jar:core.jar", 
            "-d","doc", "-sourcepath", "src", "-source", "1.7", "-docletpath", "bin", "-doclet", name, "-public", "src/tweeter/Tweeter.java"
    };
    Main.execute(name, name, args);
  }
}
